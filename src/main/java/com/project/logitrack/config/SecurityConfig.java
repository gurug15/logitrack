package com.project.logitrack.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
                // --- Public Endpoints ---
                // Allow anyone to access login and registration endpoints
                .requestMatchers("/login","/signup").permitAll() // Assuming your login controller is here

                // --- Admin-Only Endpoints ---
                // Only users with 'admin' authority can manage logistic centers
                .requestMatchers("/logistic-centers/**").hasAuthority("admin")
                // Only admins can see the admin order dashboard
                .requestMatchers("/orders/admin/**").hasAuthority("admin")

                // --- Sub-Admin-Only Endpoints ---
                // Only users with 'sub_admin' authority can access their shipments
                .requestMatchers("/shipments/center/**").hasAuthority("sub_admin")

                // --- General Authenticated Access ---
                // Any other request must be from a logged-in user (any role)
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
