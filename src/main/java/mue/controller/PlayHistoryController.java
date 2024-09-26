package mue.controller;

import mue.dto.PlayHistoryDto;
import mue.entity.PlayHistory;
import mue.service.PlayHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/playHistory")
public class PlayHistoryController {

  private final PlayHistoryService playHistoryService;

  @Autowired
  public PlayHistoryController(PlayHistoryService playHistoryService) {
    this.playHistoryService = playHistoryService;
  }

  // 1. 특정 유저의 최근 재생 기록을 모두 조회하는 GET 요청
  @GetMapping("/{userId}")
  public List<PlayHistoryDto> getRecentPlayHistory(@PathVariable String userId) {
    List<PlayHistory> playHistories = playHistoryService.getRecentPlayHistory(userId);

    // PlayHistory 엔티티를 PlayHistoryDto 리스트로 변환
    return playHistories.stream()
        .map(PlayHistoryDto::fromEntity)
        .collect(Collectors.toList());
  }

  // 2. 특정 유저의 재생 기록에 새로 추가한 재생 기록을 저장하는 POST 요청 (userId를 PathVariable로 받음)
  @PostMapping("/{userId}")
  public PlayHistoryDto addPlayHistory(@PathVariable String userId, @RequestBody PlayHistoryDto request) {
    PlayHistory playHistory = playHistoryService.savePlayHistory(
        userId,
        request.getMusicId(),
        request.getTitle(),
        request.getArtist()

    );

    return PlayHistoryDto.fromEntity(playHistory); // 저장된 엔티티를 DTO로 변환하여 반환
  }
}