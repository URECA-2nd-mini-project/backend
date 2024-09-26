package mue.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "emotion_tag")
public class EmotionTag {

  @Id
  @Column(name = "emotion_tag_id", nullable = false, unique = true)
  private String emotionTagId;

  @Column(name = "emotion_tag", nullable = false)
  private String emotionTag;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @OneToMany(mappedBy = "emotionTag", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EmotionLog> emotionLogs;

  // 기본 생성자
  public EmotionTag() {
  }

  public EmotionTag(String emotionTagId, String emotionTag, String userId, List<EmotionLog> emotionLogs) {
    this.emotionTagId = emotionTagId;
    this.emotionTag = emotionTag;
    this.userId = userId;
    this.emotionLogs = emotionLogs;
  }

  public String getEmotionTagId() {
    return emotionTagId;
  }

  public void setEmotionTagId(String emotionTagId) {
    this.emotionTagId = emotionTagId;
  }

  public String getEmotionTag() {
    return emotionTag;
  }

  public void setEmotionTag(String emotionTag) {
    this.emotionTag = emotionTag;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<EmotionLog> getEmotionLogs() {
    return emotionLogs;
  }

  public void setEmotionLogs(List<EmotionLog> emotionLogs) {
    this.emotionLogs = emotionLogs;
  }
}