package mue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mue.entity.Playlist;
import mue.entity.User; 
//User entity 사용명시하기

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDto implements Serializable {
    private String playlistId; 
    private String userId; 
    private String playlistTitle; 
    private String userImg; 
    private String contents; 

    // Playlist 엔티티로 변환하는 메서드
    public Playlist toPlaylist(User user) { // User를 참조함으로써, 각 플레이리스트가 어떤 사용자에 의해 생성되었는지 알 수 있음.

        return new Playlist(
            this.playlistId,
            user, // User 엔티티를 인자로 받아 설정
            this.playlistTitle,
            null, // 이미지의 경우 별도로 처리할 수 있음
            this.contents
        );
    }
}