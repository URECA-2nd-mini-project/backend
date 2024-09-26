package mue.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "playback_history")
public class PlayHistory implements Serializable {

  @Id
  @Column(name = "play_history_id", nullable = false, unique = true)
  private String playHistoryId;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "music_id", nullable = false)
  private String musicId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "artist", nullable = false)
  private String artist;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "played_at", nullable = false)
  private Date playedAt;

  // 기본 생성자
  public PlayHistory() {
  }

  // 모든 필드를 사용하는 생성자
  public PlayHistory(String playHistoryId, String userId, String musicId, String title, String artist, Date playedAt) {
    this.playHistoryId = playHistoryId;
    this.userId = userId;
    this.musicId = musicId;
    this.title = title;
    this.artist = artist;
    this.playedAt = playedAt;
  }

  // Getter, Setter
  public String getPlayHistoryId() {
    return playHistoryId;
  }

  public void setPlayHistoryId(String playHistoryId) {
    this.playHistoryId = playHistoryId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getMusicId() {
    return musicId;
  }

  public void setMusicId(String musicId) {
    this.musicId = musicId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public Date getPlayedAt() {
    return playedAt;
  }

  public void setPlayedAt(Date playedAt) {
    this.playedAt = playedAt;
  }
}