package mue.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "music")
public class Music {

  @Id
  @Column(name = "music_id", nullable = false, unique = true)
  private String musicId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "artist", nullable = false)
  private String artist;

  @Column(name = "duration", nullable = false)
  private int duration;

  @Column(name = "thumbnail")
  private String thumbnail;

  @Column(name = "lyrics", columnDefinition = "TEXT")
  private String lyrics;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "played_at")
  private Date playedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "playlist_id") // FK로 Playlist 테이블의 playlistId 참조
  private Playlist playlist;

  @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EmotionLog> emotionLogs;

  // 기본 생성자
  public Music() {
  }

  public Music(String musicId, String title, String artist, int duration, String thumbnail, String lyrics,
      Date playedAt, Playlist playlist, List<EmotionLog> emotionLogs) {
    this.musicId = musicId;
    this.title = title;
    this.artist = artist;
    this.duration = duration;
    this.thumbnail = thumbnail;
    this.lyrics = lyrics;
    this.playedAt = playedAt;
    this.playlist = playlist;
    this.emotionLogs = emotionLogs;
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

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public String getLyrics() {
    return lyrics;
  }

  public void setLyrics(String lyrics) {
    this.lyrics = lyrics;
  }

  public Date getPlayedAt() {
    return playedAt;
  }

  public void setPlayedAt(Date playedAt) {
    this.playedAt = playedAt;
  }

  public Playlist getPlaylist() {
    return playlist;
  }

  public void setPlaylist(Playlist playlist) {
    this.playlist = playlist;
  }

  public List<EmotionLog> getEmotionLogs() {
    return emotionLogs;
  }

  public void setEmotionLogs(List<EmotionLog> emotionLogs) {
    this.emotionLogs = emotionLogs;
  }

  // 필요한 필드를 포함한 생성자 및 Getter, Setter 생략
}