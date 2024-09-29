package mue.dto;

import mue.entity.User;
import mue.enums.Role;

// 구글 로그인 후에 구글에서 제공하는 사용자 정보들을 매핑하는 역할
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String userId;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email,
            String userId, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
            Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        System.out.println("Google Attributes: " + attributes); // 디버깅을 위한 출력
        String uuid = UUID.randomUUID().toString(); // UUID를 사용하여 고유한 userId 생성

        return OAuthAttributes.builder()
                .userId(uuid)
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .userId(userId) // 고유한 userId 생성
                .name(name)
                .gmail(email) // 'gmail'로 변경
                .photoUrl(picture) // 'picture' 대신 'photoUrl' 사용
                .role(Role.USER)
                .build();
    }
}
