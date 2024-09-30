package mue.config;

import mue.repository.UserRepository;
import mue.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
// Spring Security와 관련된 기본 임포트
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// OAuth2 관련 임포트
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

// Spring Security 관련 임포트
import org.springframework.security.web.SecurityFilterChain;

// CORS 관련 임포트
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Autowired
    public SecurityConfig(UserRepository userRepository, HttpSession httpSession,
            CustomOAuth2UserService customOAuth2UserService) {
        this.userRepository = userRepository;
        this.httpSession = httpSession;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**", "/login", "/api/**", "/oauth2/**", "/home").permitAll()
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutSuccessUrl("/"))
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession();
                            if (session.getAttribute("newUser") != null) {
                                response.sendRedirect("http://localhost:5173/EmotionBoard");
                            } else {
                                response.sendRedirect("http://localhost:5173");
                            }
                        })
                        .failureUrl("/")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserServiceBean())));

        return http.build();
    }

    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserServiceBean() {
        return new CustomOAuth2UserService(userRepository, httpSession);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}