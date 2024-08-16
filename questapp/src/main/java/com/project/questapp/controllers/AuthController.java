package com.project.questapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.questapp.entities.User;
import com.project.questapp.requests.UserRequest;
import com.project.questapp.responses.AuthResponse;
import com.project.questapp.security.JwtTokenProvider;
import com.project.questapp.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private AuthenticationManager authenticationManager;
	
	private JwtTokenProvider jwtTokenProvider;
	
	private UserService userService;
	
	private PasswordEncoder passwordEncoder;
	
	public AuthController(AuthenticationManager authenticationManager, UserService userService,PasswordEncoder passwordEncoder,JwtTokenProvider jwtTokenProvider) {
		this.authenticationManager=authenticationManager;
		this.userService= userService;
		this.passwordEncoder=passwordEncoder;
		this.jwtTokenProvider=jwtTokenProvider;
	}
	
	@PostMapping("/login")
	public AuthResponse login(@RequestBody UserRequest loginRequest) {
    	System.out.println("*****login girdi*********");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUserName(), loginRequest.getPassword());
        
        System.out.println("************"+authToken.toString());
        System.out.println("***deneme***"+authToken.getCredentials());
        
        Authentication auth = authenticationManager.authenticate(authToken);

        
        SecurityContextHolder.getContext().setAuthentication(auth);

        // JWT token'ı oluştur
        String jwtToken = jwtTokenProvider.generateJwtToken(auth);

        User user = userService.getOneUserByName(loginRequest.getUserName());
        
        // JWT token'ı başarıyla döndür
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Bearer " + jwtToken);
        authResponse.setUserId(user.getId());
        return authResponse;
	}

	
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody UserRequest registerRequest){
		AuthResponse authResponse = new AuthResponse();
		
		if(userService.getOneUserByName(registerRequest.getUserName() ) != null) {
			authResponse.setMessage("Username already in use");
			return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
		}
		User user = new User();
		user.setUserName(registerRequest.getUserName());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()) );
		userService.saveOneUser(user);
		System.out.println("*****encodededede*****"+passwordEncoder.encode(registerRequest.getPassword()));
		authResponse.setMessage("user successfully registered");
		return new ResponseEntity<>(authResponse,HttpStatus.CREATED);
	}
	
		
}