package com.test.service;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.dto.MCQRequest;

@Service
public class AIService {

	@Autowired
	private WebClient webClient;

	private static final String API_KEY = "AIzaSyAnqYE1S8nPPYQgdQvJDO2M0wFR5XCF4_A";

	private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

// 🔥 STEP 1: ONLY GENERATE (NO DB SAVE)
	public List<MCQRequest> generateMCQ(String topic) {

		try {
			String prompt = """
					Generate 10 Java MCQs on topic: """ + topic + """ 
					in sort way and 
					Strictly return JSON array:
					[
					  {
					    "question": "string",
					    "optionA": "string",
					    "optionB": "string",
					    "optionC": "string",
					    "optionD": "string",
					    "correctAnswer": "A"
					  }
					]

					Rules:
					- Exactly 4 options
					- Use keys optionA, optionB, optionC, optionD
					- correctAnswer must be A, B, C, or D
					- No explanation
					- No extra text
					""";

			Map<String, Object> requestBody = Map.of("contents",
					List.of(Map.of("parts", List.of(Map.of("text", prompt)))));

			String response = webClient.post().uri(URL + "?key=" + API_KEY).bodyValue(requestBody).retrieve()
					.bodyToMono(String.class).block();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response);

			String jsonString = root.path("candidates").get(0).path("content").path("parts").get(0).path("text")
					.asText();

			jsonString = jsonString.replace("```json", "").replace("```", "").trim();

			return mapper.readValue(jsonString, new TypeReference<List<MCQRequest>>() {
			});

		} catch (Exception e) {
			throw new RuntimeException("Error generating MCQs: " + e.getMessage());
		}
	}

}
