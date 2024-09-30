package mue.controller;

import mue.dto.EmotionTagDto;
import mue.dto.SessionUser;
import mue.entity.*;
import mue.service.EmotionTagService;
import mue.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/emotionTag")
public class EmotionTagController {

  private final UserService userService;
  private final EmotionTagService emotionTagService;
  private final HttpSession httpSession;

  @Autowired
  public EmotionTagController(UserService userService, EmotionTagService emotionTagService, HttpSession httpSession) {
    this.userService = userService;
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
    String userId = sessionUser.getUserId();

    // 사용자 ID로 감정 태그를 조회
    List<EmotionTag> emotionTags = emotionTagService.getEmotionTagsByUserId(userId);

    // EmotionTag를 EmotionTagDto로 변환하여 반환
    return emotionTags.stream()
        .map(EmotionTagDto::fromEntity) // EmotionTag를 EmotionTagDto로 변환
        .collect(Collectors.toList()); // List로 수집
  }

  // 2. 감정 태그를 등록하는 메소드
  @PostMapping
  public List<EmotionTagDto> createEmotionTags(@RequestBody List<EmotionTagDto> emotionTagDtos) {
    SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
    if (sessionUser == null) {
      throw new IllegalStateException("로그인된 사용자가 아닙니다.");
    }

    User user = userService.findById(sessionUser.getUserId());

    List<EmotionTagDto> savedEmotionTags = emotionTagDtos.stream()
        .map(emotionTagDto -> {
          EmotionTag emotionTag = EmotionTagDto.toEntity(emotionTagDto, user);
          EmotionTag savedEmotionTag = emotionTagService.createEmotionTag(emotionTag.getEmotionTag(),
              sessionUser.getUserId());
          return EmotionTagDto.fromEntity(savedEmotionTag, null);
        })
        .collect(Collectors.toList());

    return savedEmotionTags;
  }

  // 3. 감정 태그별로 감정을 기록한 음악을 조회하는 메소드
  @GetMapping("/music")
  public ResponseEntity<List<EmotionTagDto>> getMusicByEmotionTags() {
    // 감정 태그별 음악 목록을 조회하는 서비스 호출
    List<EmotionTagDto> emotionTagWithMusicList = emotionTagService.getMusicByEmotionTags();

    // 조회된 데이터를 200 OK 상태로 반환
    return new ResponseEntity<>(emotionTagWithMusicList, HttpStatus.OK);
  }
}
