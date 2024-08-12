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
	public ResponseEntity<String> login(@RequestBody UserRequest loginRequest) {
	    try {
	        System.out.println("*****login girdi*********");
	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                loginRequest.getUserName(), loginRequest.getPassword());
	        
	        System.out.println("************"+authToken.toString());
	        System.out.println("***deneme***"+authToken.getCredentials());
	        
	        Authentication auth = authenticationManager.authenticate(authToken);

	        
	        SecurityContextHolder.getContext().setAuthentication(auth);

	        // JWT token'ı oluştur
	        String jwtToken = jwtTokenProvider.generateJwtToken(auth);

	        // JWT token'ı başarıyla döndür
	        return ResponseEntity.ok("Bearer " + jwtToken);

	    } catch (AuthenticationException ex) {
	        // Kimlik doğrulama hatası
	        System.err.println("Authentication failed: " + ex);
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + ex.getMessage());

	    } catch (Exception ex) {
	        // Diğer hatalar
	        System.err.println("An error occurred: " + ex.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
	    }
	}

	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserRequest registerRequest){
		if(userService.getOneUserByName(registerRequest.getUserName()) != null)
			return new ResponseEntity<>("Username already in use", HttpStatus.BAD_REQUEST);
		
		User user = new User();
		user.setUserName(registerRequest.getUserName());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()) );
		userService.saveOneUser(user);
		System.out.println("*****encodededede*****"+passwordEncoder.encode(registerRequest.getPassword()));
		return new ResponseEntity<>("User successfully registered",HttpStatus.CREATED);
	}
	
		
}