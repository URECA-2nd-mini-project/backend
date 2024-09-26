package mue.service;

import mue.entity.PlayHistory;
import mue.repository.PlayHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PlayHistoryService {

  private final PlayHistoryRepository playHistoryRepository;

  @Autowired
  public PlayHistoryService(PlayHistoryRepository playHistoryRepository) {
    this.playHistoryRepository = playHistoryRepository;
  }

  // 1. 특정 유저의 재생 기록 저장
  public PlayHistory savePlayHistory(String userId, String musicId, String title, String artist) {
    PlayHistory playHistory = new PlayHistory(
        UUID.randomUUID().toString(), // 재생 기록 고유 ID 생성
        userId,
        musicId,
        title,
        artist,
        new Date() // 현재 시간 저장
    );
    playHistoryRepository.save(playHistory);

    return playHistory;
  }

  // 2. 특정 유저의 최근 재생 기록 상위 5개 조회
  public List<PlayHistory> getRecentPlayHistory(String userId) {
    return playHistoryRepository.findTop5ByUserIdOrderByPlayedAtDesc(userId);
  }
}