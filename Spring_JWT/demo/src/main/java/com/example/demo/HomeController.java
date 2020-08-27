package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import models.AuthenticateRepsonse;
import models.AuthenticateRequest;

@RestController
public class HomeController {
	
	@Autowired
	private AuthenticationManager authenticationManager; //used to authenticate details
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@RequestMapping("/hello")
		public String hello() {
			return "Hello from Spring";
		}
	
	@RequestMapping(value="/authenticate", method= RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticateRequest authenticationRequest) throws Exception {
		
		try {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		}
		catch(Exception e) {
			throw new Exception("Incorrect User name and password");
		}
		
		final UserDetails userdetails= userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
	
		final String jwt= jwtTokenUtil.generateToken(userdetails);
		 	
		return ResponseEntity.ok(new AuthenticateRepsonse(jwt));
	}
	
	

}
