package mue.service;

import mue.entity.*;
import mue.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlayHistoryService {

  private final PlayHistoryRepository playHistoryRepository;
  private final UserRepository userRepository;

  @Autowired
  public PlayHistoryService(PlayHistoryRepository playHistoryRepository, UserRepository userRepository) {
    this.playHistoryRepository = playHistoryRepository;
    this.userRepository = userRepository;
  }

  // 1. 특정 유저의 재생 기록 저장
  public PlayHistory savePlayHistory(String userId, String musicId, String title, String artist) {
    User user = userRepository.findByUserId(userId).get();
    PlayHistory playHistory = new PlayHistory(
        UUID.randomUUID().toString(), // 재생 기록 고유 ID 생성
        user,
        musicId,
        title,
        artist,
        new Date() // 현재 시간 저장
    );
    playHistoryRepository.save(playHistory);

    return playHistory;
  }

  // 2. 특정 유저의 최근 재생 기록 조회
  public List<PlayHistory> getRecentPlayHistory(String userId) {
    return playHistoryRepository.findTop5ByUser_UserIdOrderByPlayedAtDesc(userId);
  }

  // musicList를 PlayHistoryList로 변경
  public List<PlayHistory> createPlayHistoryList(List<Music> musicList, User user) {
    // Music 엔티티 리스트를 PlayHistory 엔티티 리스트로 변환
    return musicList.stream()
        .map(music -> PlayHistory.builder()
            .playHistoryId(UUID.randomUUID().toString()) // 고유 ID 생성
            .user(user) // 유저 정보 설정
            .musicId(music.getMusicId()) // Music 엔티티의 필드들 설정
            .title(music.getTitle())
            .artist(music.getArtist())
            .playedAt(new Date()) // 현재 시간을 playedAt으로 설정
            .build())
        .collect(Collectors.toList());
  }
}