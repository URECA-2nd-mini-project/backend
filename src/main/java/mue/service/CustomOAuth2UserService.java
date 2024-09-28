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
                OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
                OAuth2User oAuth2User = delegate.loadUser(userRequest);

                String registrationId = userRequest.getClientRegistration().getRegistrationId();
                String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                .getUserInfoEndpoint()
                                .getUserNameAttributeName();

                OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                                oAuth2User.getAttributes());

                User user = saveOrUpdate(attributes);

                httpSession.setAttribute("user", new SessionUser(user));

                return new DefaultOAuth2User(
                                Collections.singleton(new SimpleGrantedAuthority(
                                                user.getRole().getKey())),
                                attributes.getAttributes(),
                                attributes.getNameAttributeKey());
        }

        // DB에 유저 정보가 있는지 확인, 없는 경우 추가하고 이미 존재하는 경우 정보 업데이트
        private User saveOrUpdate(OAuthAttributes attributes) {
                User user = userRepository.findByUserId(attributes.getUserId()) // findByEmail을 findByGmail로 변경
                                .map(entity -> entity.update(attributes.getName(), attributes.getName(),
                                                attributes.getPicture()))
                                .orElse(attributes.toEntity());

                return userRepository.save(user);
        }

}
