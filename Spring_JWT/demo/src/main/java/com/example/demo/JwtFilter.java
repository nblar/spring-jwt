package com.example.demo;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.JwtUtil;
import com.example.demo.MyUserDetailsService;


//@Component
@Service
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private MyUserDetailsService myuserdetails;
	
	@Autowired
	private JwtUtil jwtutil;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Filter chain is used to pass on the request to other filters 
		
	final String authorizationHeader= request.getHeader("Authorization");
	
	String username=null;
	String jwt=null;
	
	
	if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")) {
		
		jwt= authorizationHeader.substring(7); //the jwt will be present after Bearer , which we are checking in "If-Else"
		username= jwtutil.extractUsername(jwt); //extract the username
	}
	
	if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null) {
		UserDetails userdetails= this.myuserdetails.loadUserByUsername(username);
		
		if(jwtutil.validateUsername(jwt, userdetails)) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userdetails, null, userdetails.getAuthorities()); //creating new username auth token
			
			usernamePasswordAuthenticationToken
			.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken); 
		//setting the above username token to context
			
		}
	}
	filterChain.doFilter(request, response); //to pass the set auth permission to other sources
		
		
	}

}
