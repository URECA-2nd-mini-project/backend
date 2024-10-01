package mue.controller;

import mue.dto.PlayHistoryDto;
import mue.dto.SessionUser;
import mue.entity.PlayHistory;
import mue.service.PlayHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/playHistory")
public class PlayHistoryController {

  private final PlayHistoryService playHistoryService;
  private final HttpSession httpSession;

  @Autowired
  public PlayHistoryController(PlayHistoryService playHistoryService, HttpSession httpSession) {
    this.playHistoryService = playHistoryService;
    this.httpSession = httpSession;
  }

  // 1. 특정 유저의 최근 재생 기록을 모두 조회하는 GET 요청
  @GetMapping
  public List<PlayHistoryDto> getRecentPlayHistory() {
    SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
    if (sessionUser == null) {
      throw new IllegalStateException("로그인된 사용자가 아닙니다.");
    }
    String userId = sessionUser.getGmail(); // NOTE Gmail 사용? 서버에서 자체적으로 발급한 ID 사용?

    List<PlayHistory> playHistories = playHistoryService.getRecentPlayHistory(userId);

    // PlayHistory 엔티티를 PlayHistoryDto 리스트로 변환
    return playHistories.stream()
        .map(PlayHistoryDto::fromEntity)
        .collect(Collectors.toList());
  }

  // 2. 특정 유저의 재생 기록에 새로 추가한 재생 기록을 저장하는 POST 요청
  @PostMapping
  public PlayHistoryDto addPlayHistory(@RequestBody PlayHistoryDto request) {
    SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
    if (sessionUser == null) {
      throw new IllegalStateException("로그인된 사용자가 아닙니다.");
    }
    String userId = sessionUser.getGmail();

    PlayHistory playHistory = playHistoryService.savePlayHistory(
        userId,
        request.getMusicId(),
        request.getTitle(),
        request.getArtist());

    return PlayHistoryDto.fromEntity(playHistory); // 저장된 엔티티를 DTO로 변환하여 반환
  }
}