package mue.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "music")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

  // Playlist 엔티티와 다대일 관계 설정
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "playlist_id", nullable = false)
  private Playlist playlist;

  // EmotionLog와의 일대다 관계 설정
  @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude // toString에서 제외 (순환 참조 방지)
  private List<EmotionLog> emotionLogs;

}