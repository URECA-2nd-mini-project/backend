package mue.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "user") // 테이블 이름을 소문자로 해주는 것이 일반적
public class User {

  @Id
  @Column(name = "user_id", nullable = false, length = 255)
  private String userId;

  @Column(name = "gmail", nullable = false, length = 255)
  private String gmail;

  @Column(name = "photo_url", length = 512)
  private String photoUrl;

  // 기본 생성자
  public User() {
  }

  // Getter, Setter
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getGmail() {
    return gmail;
  }

  public void setGmail(String gmail) {
    this.gmail = gmail;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }
}