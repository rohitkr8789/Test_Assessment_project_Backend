package com.test.service;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import javax.tools.*;

import org.springframework.stereotype.Service;

import com.test.dto.*;
import com.test.entity.*;
import com.test.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodingService {

    private final DailyTestService dailyTestService;
    private final DailyTestCodingRepository dailyTestCodingRepository;
    private final CodingQuestionRepository codingQuestionRepository;
    private final CodingTestCaseRepository codingTestCaseRepository;
    private final CodingSubmissionRepository codingSubmissionRepository;
    private final UserRepository userRepository;
    private final TestAttemptRepository testAttemptRepository;

    // ===============================
    // GET TODAY CODING QUESTIONS
    // ===============================
    public List<CodingQuestionResponse> getTodayCodingQuestions() {

        DailyTest todayTest = dailyTestService.createTodayTestIfNotExists();

        List<DailyTestCoding> mappings =
                dailyTestCodingRepository.findByDailyTestId(todayTest.getId());

        return mappings.stream()
                .map(mapping -> {
                    CodingQuestion q = mapping.getCodingQuestion();

                    return CodingQuestionResponse.builder()
                            .questionId(q.getId())
                            .title(q.getTitle())
                            .description(q.getDescription())
                            .methodSignature(q.getMethodSignature())
                            .marks(q.getMarks())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // ===============================
    // RUN CODE (RANDOM SAMPLE)
    // ===============================
    public RunCodeResponse runCode(CodingSubmissionRequest request) {

        try {
            CodingQuestion question = codingQuestionRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            String fullCode = wrapCode(request.getCode(), question.getMethodSignature());

            Class<?> clazz;
            try {
                clazz = compile(fullCode);
            } catch (Exception e) {
                return new RunCodeResponse(e.getMessage(), false, "COMPILATION_ERROR", null);
            }

            String methodName = extractMethodName(question.getMethodSignature());
            Method method = findMethod(clazz, methodName);

            // 🔥 GET ALL SAMPLE TEST CASES
            List<CodingTestCase> sampleTestCases =
                    codingTestCaseRepository
                            .findByCodingQuestionId(question.getId())
                            .stream()
                            .filter(CodingTestCase::isSample)
                            .toList();

            if (sampleTestCases.isEmpty()) {
                return new RunCodeResponse("No sample test case found", false, "DATA_ERROR", null);
            }

            // 🔥 RANDOM SELECTION
            CodingTestCase randomTestCase =
                    sampleTestCases.get(ThreadLocalRandom.current().nextInt(sampleTestCases.size()));

            String input = (request.getCustomInput() != null && !request.getCustomInput().isEmpty())
                    ? request.getCustomInput()
                    : randomTestCase.getInputData();

            ExecutorService executor = Executors.newSingleThreadExecutor();

            try {
                Future<Object> future = executor.submit(() -> {
                    try {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        Object[] params = parseInput(method, input);
                        return method.invoke(instance, params);
                    } catch (Exception e) {
                        throw new RuntimeException(
                                e.getCause() != null ? e.getCause().getMessage() : e.getMessage()
                        );
                    }
                });

                Object result;

                try {
                    result = future.get(3, TimeUnit.SECONDS);
                } catch (TimeoutException e) {
                    future.cancel(true);
                    return new RunCodeResponse("Execution timeout", false, "TIMEOUT", null);
                } catch (ExecutionException e) {
                    return new RunCodeResponse(
                            "Runtime Error: " + e.getCause().getMessage(),
                            false,
                            "RUNTIME_ERROR",
                            null
                    );
                }

                return new RunCodeResponse(String.valueOf(result), true, null, null);

            } finally {
                executor.shutdownNow();
            }

        } catch (Exception e) {
            return new RunCodeResponse(e.getMessage(), false, "UNKNOWN_ERROR", null);
        }
    }

    // ===============================
    // SUBMIT CODE (ALL TEST CASES)
    // ===============================
    public CodingSubmissionResponse executeAndCalculateScore(CodingSubmissionRequest request) throws Exception {

        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CodingQuestion question = codingQuestionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        DailyTest dailyTest = dailyTestService.createTodayTestIfNotExists();

        Optional<CodingSubmission> existingSubmission =
                codingSubmissionRepository
                        .findByCandidateIdAndDailyTestIdAndCodingQuestionId(
                                user.getId(),
                                dailyTest.getId(),
                                question.getId()
                        );

        if (existingSubmission.isPresent()) {
            throw new RuntimeException("You already submitted this coding question");
        }

        List<CodingTestCase> testCases =
                codingTestCaseRepository.findByCodingQuestionId(question.getId());

        String fullCode = wrapCode(request.getCode(), question.getMethodSignature());
        Class<?> clazz = compile(fullCode);

        String methodName = extractMethodName(question.getMethodSignature());
        Method method = findMethod(clazz, methodName);

        int passed = 0;
        int total = testCases.size();
        List<TestCaseResult> results = new ArrayList<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            int testNumber = 1;

            for (CodingTestCase testCase : testCases) {

                String input = testCase.getInputData();

                Future<Object> future = executor.submit(() -> {
                    try {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        Object[] params = parseInput(method, input);
                        return method.invoke(instance, params);
                    } catch (Exception e) {
                        return "Runtime Error";
                    }
                });

                Object result;

                try {
                    result = future.get(3, TimeUnit.SECONDS);
                } catch (TimeoutException e) {
                    future.cancel(true);
                    throw new RuntimeException("Execution timeout");
                }

                boolean isPassed = String.valueOf(result).trim()
                        .equals(testCase.getExpectedOutput().trim());

                if (isPassed) passed++;

                TestCaseResult tc = new TestCaseResult();
                tc.setTestCaseNumber(testNumber++);
                tc.setExpectedOutput(testCase.getExpectedOutput());
                tc.setActualOutput(String.valueOf(result));
                tc.setPassed(isPassed);

                results.add(tc);
            }

        } finally {
            executor.shutdownNow();
        }

        int score = (passed * question.getMarks()) / total;

        CodingSubmission submission = CodingSubmission.builder()
                .candidate(user)
                .codingQuestion(question)
                .dailyTest(dailyTest)
                .passedTestCases(passed)
                .totalTestCases(total)
                .score(score)
                .submittedAt(LocalDateTime.now())
                .build();

        codingSubmissionRepository.save(submission);

        TestAttempt attempt = testAttemptRepository
                .findByCandidateIdAndDailyTestId(user.getId(), dailyTest.getId())
                .orElseThrow(() -> new RuntimeException("MCQ not attempted yet"));

        attempt.setCodingScore(attempt.getCodingScore() + score);
        attempt.setTotalScore(attempt.getMcqScore() + attempt.getCodingScore());

        testAttemptRepository.save(attempt);

        return CodingSubmissionResponse.builder()
                .totalTestCases(total)
                .passedTestCases(passed)
                .score(score)
                .testCaseResults(results)
                .build();
    }

    // ===============================
    // STATUS METHOD
    // ===============================
    public List<CodingQuestionStatusResponse> getTodayCodingQuestionsWithStatus(Long candidateId, Long dailyTestId) {

        List<CodingQuestion> questions =
                dailyTestCodingRepository.findCodingQuestionsByDailyTestId(dailyTestId);

        List<CodingQuestionStatusResponse> result = new ArrayList<>();

        for (CodingQuestion q : questions) {

            boolean submitted = codingSubmissionRepository
                    .findByCandidateIdAndDailyTestIdAndCodingQuestionId(
                            candidateId,
                            dailyTestId,
                            q.getId()
                    ).isPresent();

            result.add(new CodingQuestionStatusResponse(
                    q.getId(),
                    q.getDescription(),
                    q.getMarks(),
                    submitted
            ));
        }

        return result;
    }

    // ===============================
    // HELPER METHODS
    // ===============================
    private String wrapCode(String userCode, String signature) {

        if (userCode.contains("public")) {
            return "public class Solution { " + userCode + " }";
        }

        return "public class Solution { " + signature + " { " + userCode + " } }";
    }

    private String extractMethodName(String signature) {
        return signature.replaceAll(".*\\s+(\\w+)\\s*\\(.*", "$1");
    }

    private Method findMethod(Class<?> clazz, String methodName) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(methodName)) return m;
        }
        throw new RuntimeException("Method not found");
    }

    private Object[] parseInput(Method method, String input) {

        Class<?>[] types = method.getParameterTypes();
        String[] parts = input.trim().split("\\s+");

        Object[] params = new Object[types.length];

        for (int i = 0; i < types.length; i++) {

            if (types[i] == int.class)
                params[i] = Integer.parseInt(parts[i]);

            else if (types[i] == String.class)
                params[i] = parts[i];

            else
                throw new RuntimeException("Unsupported type");
        }

        return params;
    }

    private Class<?> compile(String code) throws Exception {

        Path tempDir = Files.createTempDirectory("compile");

        File file = new File(tempDir.toFile(), "Solution.java");
        Files.writeString(file.toPath(), code);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(diagnostics, null, null);

        Iterable<? extends JavaFileObject> units =
                fileManager.getJavaFileObjectsFromFiles(List.of(file));

        boolean success = compiler.getTask(null, fileManager, diagnostics, null, null, units).call();

        fileManager.close();

        if (!success) {
            String error = diagnostics.getDiagnostics()
                    .stream()
                    .map(d -> d.getMessage(null))
                    .collect(Collectors.joining("\n"));
            throw new RuntimeException(error);
        }

        URLClassLoader classLoader =
                URLClassLoader.newInstance(new URL[]{tempDir.toUri().toURL()});

        return Class.forName("Solution", true, classLoader);
    }
}