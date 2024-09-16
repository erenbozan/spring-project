package com.project.questapp.responses;

import lombok.Data;

@Data
public class AuthResponse {
	
	String message;
	String accessToken;
	String refreshToken;
	Long userId;

}
