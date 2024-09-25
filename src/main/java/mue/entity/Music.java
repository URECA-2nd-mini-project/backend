package mue.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "music")
public class Music implements Serializable {

  @Id
  @Column(name = "music_id", nullable = false, unique = true)
  private String musicId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "singer", nullable = false)
  private String singer;

  @Column(name = "length", nullable = false)
  private int length;

  @Column(name = "thumbnail")
  private String thumbnail;

  @Column(name = "lyrics", columnDefinition = "TEXT")
  private String lyrics;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "playlist_id", nullable = false) // FK로 Playlist 테이블의 playlistId 참조
  private Playlist playlist;

  // 기본 생성자
  public Music() {
  }

  // 생성자 (필요에 따라 추가)
  public Music(String musicId, String name, String singer, int length, String thumbnail, String lyrics,
      Playlist playlist) {
    this.musicId = musicId;
    this.name = name;
    this.singer = singer;
    this.length = length;
    this.thumbnail = thumbnail;
    this.lyrics = lyrics;
    this.playlist = playlist;
  }

  // Getter, Setter
  public String getMusicId() {
    return musicId;
  }

  public void setMusicId(String musicId) {
    this.musicId = musicId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSinger() {
    return singer;
  }

  public void setSinger(String singer) {
    this.singer = singer;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
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

  public Playlist getPlaylist() {
    return playlist;
  }

  public void setPlaylist(Playlist playlist) {
    this.playlist = playlist;
  }
}