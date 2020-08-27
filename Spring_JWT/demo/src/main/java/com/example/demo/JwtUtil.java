package com.example.demo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
 private static final String SECRET_KEY = "JAVA_JWT_DEMO"; //the complexier the better

//used to create new JWT, given user details object.
	
 
 public String extractUsername(String token) { // extracts username from token
	 return extractClaim(token, Claims::getSubject);
 }
 
 public Date extractExpiration(String token) { //returns expiration date of the session/token
	 return extractClaim(token, Claims::getExpiration);
 }
 
 
 
 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	 final Claims claims=extractallclaims(token);
	 return claimsResolver.apply(claims);
	
 }
 
 
	private Claims extractallclaims(String token) { //used to extract the body of the json token
		return  Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
}


	public boolean isExpired(String token) { //checks if the token is expired or not
		return extractExpiration(token).before(new Date());
	}
	
	public boolean validateUsername(String token, UserDetails userdetails) { 
		//used to check if the username in the token is same as that in the obtained userdetails or not
		final String username= extractUsername(token);
		return (username.equals(userdetails.getUsername()) && !isExpired(token));
		//also validates the whether token is expired or not
	}
	
	public String generateToken(UserDetails userDetails) {
		
		Map<String, Object> claims= new HashMap<>();
		return createToken(claims, userDetails.getUsername());
		//used to create token based on username and userdetails in a map.
	}

private String createToken(Map<String, Object> claims, String subject) {
	 
	return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis()+ 1000* 60 *60 *10)) //10 hrs of expiration date
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	
   }
}
