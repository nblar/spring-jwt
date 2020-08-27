package models;

public class AuthenticateRepsonse { // to send response from the application to the user. 
	
	private final String jwt;

	
	public AuthenticateRepsonse(String jwt) {
	
	this.jwt = jwt;
}


	public String getJwt() {
		return jwt;
	} 
	
}
