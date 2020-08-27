package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{

	@Autowired
	MyUserDetailsService myuserDetailsService;
	
	@Autowired
	private JwtFilter jwtRequestFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		
		auth.userDetailsService(myuserDetailsService);
	}
	
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests().antMatchers("/authenticate").permitAll()
		.anyRequest().authenticated()
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//tell the spring to not bother to create the session. 
	
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		// enabling that jwtRequestFilter is called before UsrnamePasswordFilter
	}




	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception { //used to be a default which is removed from spring 3
		return super.authenticationManagerBean();
	}


  

	@Bean 
	public PasswordEncoder passwordencoder() { //to encode our password
		return NoOpPasswordEncoder.getInstance(); // to not do any hashing
	}
	
	

}
