package com.project.logitrack.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	
	@Autowired
	private UserDetailsService userdetailService;
	
	@Autowired
	private JwtFilter jwtFilter; 
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
            .cors(Customizer.withDefaults()) // Use the bean for CORS config
            .csrf(customizer -> customizer.disable()) // Disable CSRF for stateless API
            .authorizeHttpRequests(req -> req
                .requestMatchers("/login","/signup").permitAll() // Assuming your login controller is here
                .requestMatchers(HttpMethod.GET, "/logistic-centers").permitAll()
                .requestMatchers("/logistic-centers/**").hasAuthority("admin")
                .requestMatchers("/orders/admin/**").hasAuthority("admin")
                .requestMatchers("/shipments/my-center").hasAuthority("sub_admin")
                .requestMatchers("/api/subadmin/**").hasAuthority("sub_admin")
                .requestMatchers("/shipments/center/**").hasAuthority("sub_admin")
                .requestMatchers("/users/profile/me").authenticated()
                .anyRequest().authenticated() 
            )	
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessions
            .authenticationProvider(authProvider()) // Set your custom auth provider
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Add your JWT filter
		
		return http.build();
	}
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
	    config.setAllowedOrigins(List.of("http://localhost:5173")); // Replace with your frontend origin
	    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    config.setAllowedHeaders(List.of("*"));
	    config.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);
	    return (CorsConfigurationSource)source;
	}
	
	
	@Bean
	public AuthenticationProvider authProvider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userdetailService);
		
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
}
