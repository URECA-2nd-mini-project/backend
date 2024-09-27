package mue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mue.dto.ApiResponseDto;
import mue.dto.PlaylistDto;
import mue.entity.Playlist;
import mue.entity.User; // User 엔티티 임포트
import mue.service.ImageService;
import mue.service.PlaylistService;
import mue.service.UserService; // UserService 임포트

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService; // ImageService 주입
    private final PlaylistService playlistService; // PlaylistService 주입
    private final UserService userService; // UserService 주입

    @Autowired
        public ImageController(ImageService imageService, PlaylistService playlistService, UserService userService ) {

        this.imageService = imageService;
        this.playlistService = playlistService;
        this.userService = userService; 
    }

    // 이미지 업로드 메소드
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponseDto> uploadImage(
            @RequestParam MultipartFile file,
            @RequestParam String playlistId,
            @RequestParam String userId,
            @RequestParam String playlistTitle,
            @RequestParam String contents) {
        
        String userImgPath = imageService.saveImage(file); // 로컬에 이미지 저장 (ImageService에서 saveImage 메서드가 uploads 폴더에 이미지를 저장)

        // 업로드 후 반영하는 PlaylistDto 생성
        PlaylistDto playlistDto = new PlaylistDto(
                playlistId,
                userId,
                playlistTitle,
                userImgPath,
                contents
        );

        // User 객체 가져오기 (DB에서 조회)
        // User user = userService.findById(userId); // userId로 User 객체를 가져오는 메서드

        // 플레이리스트 업데이트 (null> user객체 가져오면 수정)
        Playlist updatedPlaylist = playlistService.updatePlaylist(playlistId, playlistDto.toPlaylist(null));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto(true, "Image uploaded successfully", updatedPlaylist));
    }

    // 이미지 수정 메소드
    @PutMapping("/{imageId}")
    public ResponseEntity<ApiResponseDto> updateImage(
            @PathVariable String imageId,
            @RequestParam MultipartFile file,
            @RequestParam String playlistId,
            @RequestParam String userId,
            @RequestParam String playlistTitle,
            @RequestParam String contents) {
        
        String userImgPath = imageService.updateImage(imageId, file); // 이미지 수정

        // 수정 후 반영하는 PlaylistDto 생성
        PlaylistDto playlistDto = new PlaylistDto(
                playlistId,
                userId,
                playlistTitle,
                userImgPath,
                contents
        );

        // User 객체 가져오기 (DB에서 조회)
        // User user = userService.findById(userId); // userId로 User 객체를 가져오는 메서드

        // 플레이리스트 업데이트 (정상적으로 수정 완료 200 ok 상태) (null> user객체 가져오면 수정)
        Playlist updatedPlaylist = playlistService.updatePlaylist(playlistId, playlistDto.toPlaylist(null));
        return ResponseEntity.ok(new ApiResponseDto(true, "Image updated successfully", updatedPlaylist));
    }

    @DeleteMapping("/{imageId}")
public ResponseEntity<ApiResponseDto> deleteImage(
        @PathVariable String imageId,
        @RequestParam String playlistId) {
    
    // 이미지 삭제
    imageService.deleteImage(imageId);

    // 플레이리스트를 ID로 조회
    Playlist playlist = playlistService.getPlaylistById(playlistId)
            .orElseThrow(() -> new RuntimeException("Playlist not found")); // 예외 처리

    // 이미지 경로 제거
    if (playlist.getUserImg() != null) { // userImg가 존재할 경우
        playlist.setUserImg(null); // null로 설정
        playlistService.updatePlaylist(playlistId, playlist); // 플레이리스트 업데이트
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // HTTP 204 No Content 반환
}
}


