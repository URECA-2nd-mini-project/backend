package mue.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "play_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayHistory {

  @Id
  @Column(name = "play_history_id", nullable = false, unique = true)
  private String playHistoryId;

  // User 엔티티와 다대일 관계 설정
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false) // 외래 키로 user_id 사용
  private User user;

  @Column(name = "music_id", nullable = false)
  private String musicId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "artist", nullable = false)
  private String artist;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "played_at", nullable = false)
  private Date playedAt;
}