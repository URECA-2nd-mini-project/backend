package mue.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "playlist")
public class Playlist implements Serializable {

  @Id
  @Column(name = "playlist_id", nullable = false, unique = true)
  private String playlistId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false) // FK로 User 테이블의 id 참조
  private User user; // User 엔티티로 설정

  @Column(name = "name", nullable = false)
  private String name;

  @Lob // 대용량 데이터로 처리 (파일, 이미지 등)
  @Column(name = "cover")
  private byte[] cover; // 파일을 BLOB으로 저장

  @Column(name = "contents", columnDefinition = "TEXT")
  private String contents;

  // 기본 생성자
  public Playlist() {
  }

  // 생성자 (필요에 따라 추가)
  public Playlist(String playlistId, User user, String name, byte[] cover, String contents) {
    this.playlistId = playlistId;
    this.user = user;
    this.name = name;
    this.cover = cover;
    this.contents = contents;
  }

  // Getter, Setter
  public String getPlaylistId() {
    return playlistId;
  }

  public void setPlaylistId(String playlistId) {
    this.playlistId = playlistId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getCover() {
    return cover;
  }

  public void setCover(byte[] cover) {
    this.cover = cover;
  }

  public String getContents() {
    return contents;
  }

  public void setContents(String contents) {
    this.contents = contents;
  }
}