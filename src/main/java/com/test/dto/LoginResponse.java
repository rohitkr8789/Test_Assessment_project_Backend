package com.test.dto;

import java.time.LocalDate;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginResponse {

    private String token;
    private String role;
    private String name;
    private String email;
    private String imageUrl;
    private LocalDate dateOfBirth;

    public LoginResponse(String token, String role, String name, String email, String imageUrl, LocalDate dateOfBirth) {
        this.token = token;
        this.role = role;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.dateOfBirth = dateOfBirth;
    }

	public String getToken() {
		return token;
	}

	public String getRole() {
		return role;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	
	
	

    
}