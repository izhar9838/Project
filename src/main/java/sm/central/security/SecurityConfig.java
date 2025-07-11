package sm.central.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import sm.central.security.filter.JwtFilter;
import sm.central.security.filter.RoleFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private UserDetailsService detailsService;
	private JwtFilter jwtFilter;
    private JwtUtil jwtUtil;
    public SecurityConfig(UserDetailsService detailsService, JwtFilter jwtFilter, JwtUtil jwtUtil) {
        this.detailsService = detailsService;
        this.jwtFilter = jwtFilter;
        this.jwtUtil = jwtUtil;
    }
   @SuppressWarnings("removal")
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationManager authenticationManager) throws Exception {
	   RoleFilter roleFilter = new RoleFilter(authenticationManager, jwtUtil);
       http.cors(withDefaults())
               .csrf(AbstractHttpConfigurer::disable)

               .authorizeHttpRequests(req -> req.
                       requestMatchers("/api/public/**").permitAll()
                       .requestMatchers("/api/public/health").permitAll()
                       .requestMatchers("/api/student/**").hasRole("student")
                       .requestMatchers("/api/teacher/**").hasRole("teacher")
                       .requestMatchers("/api/admin/**").hasRole("admin")
                       .anyRequest().authenticated()).
               sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .addFilterBefore(roleFilter, UsernamePasswordAuthenticationFilter.class)
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);;
	   
	   	
	   return http.build();
   }
   @Bean
   public AuthenticationProvider authenticationProvider() {
	   DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
	   provider.setPasswordEncoder(passwordEncoder());
	   provider.setUserDetailsService(detailsService);
	   return provider;
   }
   @Bean 
   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
	   return config.getAuthenticationManager();
   }
   @Bean
   public PasswordEncoder passwordEncoder() {
	   return new BCryptPasswordEncoder(14);
   }
}