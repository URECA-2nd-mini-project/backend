package mue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mue.entity.Playlist;
import mue.entity.User;
//User entity 사용명시하기

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistDto implements Serializable {
    private String playlistId;
    private String playlistTitle;
    private String userImg;
    private MultipartFile userImgFile;
    private String contents;
    private List<PlayHistoryDto> musics;

    // Playlist 엔티티로 변환하는 메서드
    public Playlist toPlaylist(User user, String userImgPath) { // 파일 경로를 인자로 받음
        return Playlist.builder()
                .playlistId(UUID.randomUUID().toString())
                .user(user) // 전달받은 User 엔티티 설정
                .playlistTitle(this.playlistTitle)
                .userImgPath(userImgPath) // 파일 경로를 userImg에 저장
                .contents(this.contents)
                .build();
    }

    // Playlist 엔티티에서 데이터를 추출하여 DTO로 변환하는 메서드
    public static PlaylistDto fromPlaylist(Playlist playlist, List<PlayHistoryDto> musics) {
        return PlaylistDto.builder()
                .playlistId(playlist.getPlaylistId())
                .playlistTitle(playlist.getPlaylistTitle())
                .userImg(playlist.getUserImgPath()) // 이미지 경로 설정
                .contents(playlist.getContents()) // 컨텐츠 설정
                .musics(musics) // 컨텐츠 설정
                .build();
    }

    // Playlist 엔티티에서 데이터를 추출하여 DTO로 변환하는 메서드
    public static PlaylistDto fromPlaylist(Playlist playlist) {
        return PlaylistDto.builder()
                .playlistId(playlist.getPlaylistId())
                .playlistTitle(playlist.getPlaylistTitle())
                .userImg(playlist.getUserImgPath()) // 이미지 경로 설정
                .contents(playlist.getContents()) // 컨텐츠 설정
                .build();
    }
}