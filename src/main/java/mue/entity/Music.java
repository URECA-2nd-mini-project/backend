package mue.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "music")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Music {

  @Id
  @Column(name = "music_no", nullable = false, unique = true)
  private String musicNo;

  @Column(name = "music_id")
  private String musicId;

  @Column(name = "title")
  private String title;

  @Column(name = "artist")
  private String artist;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "played_at")
  private Date playedAt;

  // Playlist 엔티티와 다대일 관계 설정
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "playlist_id", nullable = true)
  private Playlist playlist;

  // EmotionLog와의 일대다 관계 설정
  @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude // toString에서 제외 (순환 참조 방지)
  private List<EmotionLog> emotionLogs;

  // 엔티티 생성 시, ID 수동 할당
  @PrePersist
  public void generateId() {
    if (this.musicNo == null) {
      this.musicNo = UUID.randomUUID().toString();
    }
  }
}