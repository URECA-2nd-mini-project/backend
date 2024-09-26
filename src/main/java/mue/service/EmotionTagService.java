package mue.service;

import mue.entity.EmotionTag;
import mue.entity.EmotionLog;
import mue.repository.EmotionTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmotionTagService {

  private final EmotionTagRepository emotionTagRepository;

  @Autowired
  public EmotionTagService(EmotionTagRepository emotionTagRepository) {
    this.emotionTagRepository = emotionTagRepository;
  }

  // 1. 감정 태그 생성 및 저장
  public EmotionTag createEmotionTag(String emotionTag, String userId) {
    // UUID를 이용해 고유 ID 생성
    String emotionTagId = UUID.randomUUID().toString();

    // 빈 감정 로그 리스트 초기화
    List<EmotionLog> emptyEmotionLogs = List.of();

    // 새로운 감정 태그 생성
    EmotionTag newEmotionTag = new EmotionTag(
        emotionTagId, // 생성된 고유 ID
        emotionTag, // 전달받은 감정 태그
        userId, // 전달받은 유저 ID
        emptyEmotionLogs // 빈 감정 기록 리스트
    );

    // 감정 태그 저장
    return emotionTagRepository.save(newEmotionTag);
  }

  // 2. 특정 유저의 모든 감정 태그 조회
  public List<EmotionTag> getEmotionTagsByUserId(String userId) {
    return emotionTagRepository.findAllByUserId(userId);
  }
}