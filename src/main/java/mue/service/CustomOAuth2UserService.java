// OAuth 2.0 로그인 과정에서 사용자 정보를 가져오는 역할
package mue.service;

import mue.dto.OAuthAttributes;
import mue.dto.SessionUser;
import mue.entity.User;
import mue.repository.UserRepository;
// 전체적인 보안 설정을 관리
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        private final UserRepository userRepository;
        private final HttpSession httpSession;

        @Autowired
        public CustomOAuth2UserService(UserRepository userRepository, HttpSession httpSession) {
                this.userRepository = userRepository;
                this.httpSession = httpSession;
        }

        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                // OAuth2UserService 위임 설정
                OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
                OAuth2User oAuth2User = delegate.loadUser(userRequest);

                // 로그인하는 OAuth 서비스의 id (예: google, naver 등)
                String registrationId = userRequest.getClientRegistration().getRegistrationId();

                // OAuth 서비스에서 유저의 고유한 id를 받아오기 위한 attribute 명
                String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                .getUserInfoEndpoint().getUserNameAttributeName();

                // OAuthAttributes 객체에 OAuth 서비스에서 받아온 유저 정보를 담기
                OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                                oAuth2User.getAttributes());

                // User 테이블에 이미 존재하는 유저인지 확인
                Optional<User> userOptional = userRepository.findByGmail(attributes.getEmail());

                if (userOptional.isPresent()) {
                        // 기존 유저인 경우 세션에 저장
                        User user = userOptional.get();
                        httpSession.setAttribute("user", new SessionUser(user)); // 세션에 기존 유저 정보 저장
                } else {
                        // 새로운 유저인 경우 세션에 newUser로 저장
                        httpSession.setAttribute("newUser", attributes); // 세션에 새 유저 정보 저장
                }

                // 최종 OAuth2User 객체 반환
                return new DefaultOAuth2User(
                                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                                attributes.getAttributes(),
                                attributes.getNameAttributeKey());
        }

        // DB에 유저 정보가 있는지 확인, 없는 경우 추가하고 이미 존재하는 경우 정보 업데이트
        private User saveOrUpdate(OAuthAttributes attributes) {
                User user = userRepository.findByGmail(attributes.getEmail())
                                .orElse(null);

                if (user != null) {
                        // 기존 사용자 정보 업데이트
                        user.update(attributes.getName(), attributes.getEmail(), attributes.getPicture());
                        return userRepository.save(user);
                } else {
                        // 새로운 사용자 생성
                        return userRepository.save(attributes.toEntity());
                }
        }

}
