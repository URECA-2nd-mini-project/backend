package mue.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //모든 필드에 대한 Getter, Setter, toString, equals, hashCode 메서드를 자동으로 생성
@NoArgsConstructor //인자가 없는 기본 생성자를 생성
@AllArgsConstructor // 필드를 인자로 받는 생성자를 생성
@Entity
@Table(name = "playlist")
public class Playlist implements Serializable {

    @Id
    @Column(name = "playlist_id", nullable = false, unique = true)
    private String playlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // FK로 User 테이블의 id 참조
    private User user; // User 엔티티로 설정

    @Column(name = "playlist_title", nullable = false)
    private String playlistTitle;

    @Column(name = "user_img") // 사용자 이미지 URL
    private String userImg; // 사용자 이미지 URL을 저장

    @Column(name = "contents", columnDefinition = "TEXT")
    private String contents;

}