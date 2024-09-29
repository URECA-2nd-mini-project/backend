package mue.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "playlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Playlist {

    @Id
    @Column(name = "playlist_id", nullable = false, unique = true)
    private String playlistId;

    @Column(name = "playlist_title", nullable = false)
    private String playlistTitle;

    @Column(name = "user_img")
    private String userImgPath; // URL로 저장 (필드명에서 _를 제거)

    @Column(name = "contents", columnDefinition = "TEXT")
    private String contents;

    // Music 엔티티와의 일대다 관계 설정
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // toString에서 제외 (순환 참조 방지)
    private List<Music> musicList;

    // User 엔티티와 다대일 관계 설정 (필요시 설정)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}