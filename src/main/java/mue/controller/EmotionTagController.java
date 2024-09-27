package mue.controller;

import mue.dto.EmotionTagDto;
import mue.entity.EmotionTag;
import mue.service.EmotionTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emotionTag")
public class EmotionTagController {

  private final EmotionTagService emotionTagService;

  @Autowired
  public EmotionTagController(EmotionTagService emotionTagService) {
    this.emotionTagService = emotionTagService;
  }

  // 1. 특정 유저의 감정 태그를 모두 조회하는 메소드
  @GetMapping("/{userId}")
  public List<EmotionTagDto> getEmotionTagsByUser(@PathVariable String userId) {
    List<EmotionTag> emotionTags = emotionTagService.getEmotionTagsByUserId(userId);

    return emotionTags.stream()
        .map(EmotionTagDto::fromEntity)
        .collect(Collectors.toList());
  }

  // 2. 감정 태그를 등록하는 메소드
  @PostMapping("/{userId}")
  public EmotionTagDto createEmotionTag(@RequestBody EmotionTagDto emotionTagDto, @PathVariable String userId) {
    EmotionTag emotionTag = EmotionTagDto.toEntity(emotionTagDto);

    EmotionTag savedEmotionTag = emotionTagService.createEmotionTag(emotionTag.getEmotionTag(), userId);
    return EmotionTagDto.fromEntity(savedEmotionTag);
  }
}
