package mue.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "emotion_tag")
public class EmotionTag implements Serializable {

  @Id
  @Column(name = "emotion_tag_id", nullable = false, unique = true)
  private String emotionTagId;

  @Column(name = "emotion_tag", nullable = false)
  private String emotionTag;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false) // FK로 User 테이블의 userId 참조
  private User user;

  // 기본 생성자
  public EmotionTag() {
  }

  // 생성자 (필요에 따라 추가)
  public EmotionTag(String emotionTagId, String emotionTag, User user) {
    this.emotionTagId = emotionTagId;
    this.emotionTag = emotionTag;
    this.user = user;
  }

  // Getter, Setter
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}