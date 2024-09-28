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
    private final UserRepository userRepository; // 의존성 주입
    private final HttpSession httpSession; // 의존성 주입

    @Autowired
    public SecurityConfig(UserRepository userRepository, HttpSession httpSession,
            CustomOAuth2UserService customOAuth2UserService) {
        this.userRepository = userRepository;
        this.httpSession = httpSession;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/api/**", "/oauth2/**", "/home").permitAll()
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("http://localhost:5173/EmotionBoard") // 로그인 성공 후 리다이렉트할 프론트엔드 URL
                        .failureUrl("/") // 로그인 실패 시 리다이렉트할 URL
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserServiceBean()) // OAuth2 사용자 정보를 처리
                        ));

        return http.build();
    }

    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserServiceBean() {
        return new CustomOAuth2UserService(userRepository, httpSession);
    }

    // CORS 설정
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173") // React Vite 주소 접근 허용
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true); // 세션 쿠키를 주고받기 위해 Credential 허용
            }
        };
    }
}