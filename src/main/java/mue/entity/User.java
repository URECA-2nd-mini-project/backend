package mue.entity;

import jakarta.persistence.*;
import lombok.*;
import mue.enums.Role;

import java.util.List;

@Entity
@Table(name = "user")
@Data // @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode를 포함
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 필드를 포함한 생성자 생성
@Builder
public class User {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "gmail", nullable = false)
    private String gmail;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "photo_url")
    private String photoUrl;

    @Enumerated(EnumType.STRING) // Enum 값 저장
    @Column(nullable = false)
    private Role role;

    // EmotionTag 테이블과 일대다 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // toString에서 제외 (순환 참조 방지)
    private List<EmotionTag> emotionTags;

    // Playlist와의 일대다 관계 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // 순환 참조 방지
    private List<Playlist> playlists;

    public User update(String name, String gmail, String photoUrl) {
        this.name = name;
        this.gmail = gmail;
        this.photoUrl = photoUrl;
        return this;
    }
}