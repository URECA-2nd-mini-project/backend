package mue.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "emotion_log")
public class EmotionLog implements Serializable {

  @Id
  @Column(name = "emotion_log_id", nullable = false, unique = true)
  private String emotionLogId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "music_id", nullable = false) // FK로 Music 테이블의 musicId 참조
  private Music music;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "emotion_tag_id", nullable = false) // FK로 EmotionTag 테이블의 emotionTagId 참조
  private EmotionTag emotionTag;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false) // FK로 User 테이블의 userId 참조
  private User user;

  @Column(name = "contents", columnDefinition = "TEXT", nullable = false)
  private String contents;

  @Temporal(TemporalType.TIMESTAMP) // 날짜 및 시간 데이터를 나타내기 위한 어노테이션
  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  // 기본 생성자
  public EmotionLog() {
  }

  // 생성자 (필요에 따라 추가)
  public EmotionLog(String emotionLogId, Music music, EmotionTag emotionTag, User user, String contents,
      Date createdAt) {
    this.emotionLogId = emotionLogId;
    this.music = music;
    this.emotionTag = emotionTag;
    this.user = user;
    this.contents = contents;
    this.createdAt = createdAt;
  }

  // Getter, Setter
  public String getEmotionLogId() {
    return emotionLogId;
  }

  public void setEmotionLogId(String emotionLogId) {
    this.emotionLogId = emotionLogId;
  }

  public Music getMusic() {
    return music;
  }

  public void setMusic(Music music) {
    this.music = music;
  }

  public EmotionTag getEmotionTag() {
    return emotionTag;
  }

  public void setEmotionTag(EmotionTag emotionTag) {
    this.emotionTag = emotionTag;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getContents() {
    return contents;
  }

  public void setContents(String contents) {
    this.contents = contents;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}