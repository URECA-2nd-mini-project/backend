package mue.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "emotion_tag")
@Data // @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode를 포함
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 필드를 포함한 생성자 생성
@Builder
public class EmotionTag {

  @Id
  @Column(name = "emotion_tag_id", nullable = false, unique = true)
  private String emotionTagId;

  @Column(name = "emotion_tag", nullable = false)
  private String emotionTag;

  // User와 다대일 관계 설정
  @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 사용
  @JoinColumn(name = "user_id", nullable = false) // 외래키 설정
  @ToString.Exclude // toString에서 제외 (순환 참조 방지)
  private User user;

  // EmotionLog와 일대다 관계 설정
  @OneToMany(mappedBy = "emotionTag", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude // toString에서 제외 (순환 참조 방지)
  private List<EmotionLog> emotionLogs;
}