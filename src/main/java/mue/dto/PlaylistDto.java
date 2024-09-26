package mue.dto;

import java.io.Serializable;

public class PlaylistDto implements Serializable {

    private String playlistId;
    private String userId; // 사용자 ID를 포함
    private String name;
    private String contents;

    // 기본 생성자
    public PlaylistDto() {
    }

    // 생성자
    public PlaylistDto(String playlistId, String userId, String name, String contents) {
        this.playlistId = playlistId;
        this.userId = userId;
        this.name = name;
        this.contents = contents;
    }

    // Getter 및 Setter
    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}