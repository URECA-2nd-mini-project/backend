package mue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mue.entity.Playlist;
import mue.entity.User;
//User entity 사용명시하기

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistDto implements Serializable {
    private String playlistId;
    private User user; // User 엔티티의 ID만 보유
    private String playlistTitle;
    private MultipartFile userImg; // 파일로 변경
    private String contents;

    // Playlist 엔티티로 변환하는 메서드
    public Playlist toPlaylist(User user, String userImgPath) { // 파일 경로를 인자로 받음
        return Playlist.builder()
                .playlistId(this.playlistId)
                .user(user) // 전달받은 User 엔티티 설정
                .playlistTitle(this.playlistTitle)
                .userImg(userImgPath) // 파일 경로를 userImg에 저장
                .contents(this.contents)
                .build();
    }
}