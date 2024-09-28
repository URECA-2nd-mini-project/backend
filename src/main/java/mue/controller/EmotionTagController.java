package mue.controller;

import mue.dto.EmotionTagDto;
import mue.dto.SessionUser;
import mue.entity.EmotionTag;
import mue.service.EmotionTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emotionTag")
public class EmotionTagController {

  private final EmotionTagService emotionTagService;
  private final HttpSession httpSession;

  @Autowired
  public EmotionTagController(EmotionTagService emotionTagService, HttpSession httpSession) {
    this.emotionTagService = emotionTagService;
    this.httpSession = httpSession;
  }

  // 1. 특정 유저의 감정 태그를 모두 조회하는 메소드
  @GetMapping
  public List<EmotionTagDto> getEmotionTagsByUser() {
    // 세션에서 사용자 정보를 가져옴
    SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
    if (sessionUser == null) {
      throw new IllegalStateException("로그인된 사용자가 아닙니다.");
    }
    String userId = sessionUser.getUser().getUserId(); // NOTE Gmail 사용? 서버에서 자체적으로 발급한 ID 사용?
    List<EmotionTag> emotionTags = emotionTagService.getEmotionTagsByUserId(userId);

    return emotionTags.stream()
        .map(EmotionTagDto::fromEntity)
        .collect(Collectors.toList());
  }

  // 2. 감정 태그를 등록하는 메소드
  @PostMapping
  public EmotionTagDto createEmotionTag(@RequestBody EmotionTagDto emotionTagDto) {
    // 세션에서 사용자 정보를 가져옴
    SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
    if (sessionUser == null) {
      throw new IllegalStateException("로그인된 사용자가 아닙니다.");
    }

    EmotionTag emotionTag = EmotionTagDto.toEntity(emotionTagDto, sessionUser.getUser());

    EmotionTag savedEmotionTag = emotionTagService.createEmotionTag(emotionTag.getEmotionTag(),
        sessionUser.getUser().getUserId());
    return EmotionTagDto.fromEntity(savedEmotionTag);
  }
}
