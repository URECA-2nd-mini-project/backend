package mue.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import mue.service.*;
import mue.dto.ApiResponseDto;
import mue.dto.PlayHistoryDto;
import mue.dto.PlaylistDto;
import mue.dto.SessionUser;
import mue.entity.*;
import mue.repository.*;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MusicService musicService;
    @Autowired
    private PlayHistoryService playHistoryService;

    @Autowired
    private HttpSession httpSession;

    // 1. 플레이리스트 생성
    @PostMapping
    public ResponseEntity<PlaylistDto> createPlaylist(@RequestBody PlaylistDto playlistDto) {
        // 세션에서 현재 유저의 정보를 가져옴
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        // 세션에서 가져온 유저 정보를 통해 User 객체 조회
        User user = userService.findById(sessionUser.getUserId());

        // userImg는 생성 시 null로 설정
        Playlist playlist = playlistDto.toPlaylist(user, null);

        // Playlist 저장
        Playlist createdPlaylist = playlistService.createPlaylist(playlist);

        // 저장된 Playlist를 PlaylistDto로 변환
        PlaylistDto responseDto = PlaylistDto.fromPlaylist(createdPlaylist);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 2. 모든 플레이리스트 조회
    @GetMapping
    public ResponseEntity<List<PlaylistDto>> getAllPlaylistsByUser() {
        // 세션에서 현재 유저의 정보를 가져옴
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        if (sessionUser == null) {
            // 유저가 로그인되어 있지 않다면 401 Unauthorized 응답 반환
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // 세션에서 가져온 유저의 ID로 플레이리스트 조회
        List<Playlist> playlists = playlistService.getAllPlaylistsByUserId(sessionUser.getUserId());

        // Playlist 엔티티 리스트를 PlaylistDto 리스트로 변환
        List<PlaylistDto> playlistDtos = playlists.stream()
                .map(PlaylistDto::fromPlaylist)
                .collect(Collectors.toList());

        return new ResponseEntity<>(playlistDtos, HttpStatus.OK);
    }

    // 3. 플레이리스트 세부 정보 조회
    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistDto> getPlaylistInfoById(@PathVariable String playlistId) {
        Playlist playlist = playlistService.getPlaylistById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userService.findById(sessionUser.getUserId());

        // 1. MusicService에서 사용자와 플레이리스트에 따른 음악 리스트 가져오기
        List<Music> musics = musicService.getMusicByUserAndPlaylist(sessionUser.getUserId(), playlistId);

        // 2. Music 엔티티 리스트를 PlayHistoryDto 리스트로 변환
        List<PlayHistory> playHistories = playHistoryService.createPlayHistoryList(musics, user);

        // 3. Music 엔티티 리스트를 PlayHistoryDto 리스트로 변환
        List<PlayHistoryDto> playHistoryResponse = playHistories.stream()
                .map(PlayHistoryDto::fromEntity) // fromEntity 메서드를 이용해 변환
                .collect(Collectors.toList());

        // 3. Playlist 엔티티를 PlaylistDto로 변환, 그리고 음악 리스트 추가
        PlaylistDto playlistDto = PlaylistDto.fromPlaylist(playlist, playHistoryResponse);

        // 4. 변환된 PlaylistDto를 ResponseEntity로 감싸서 반환
        return new ResponseEntity<>(playlistDto, HttpStatus.OK);
    }

    // 4. 플레이리스트 수정 (파일을 업로드할 수 있음)
    @PutMapping(value = "/{playlistId}", consumes = { "multipart/form-data" })
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable String playlistId,
            @ModelAttribute PlaylistDto playlistDto) {

        // 기존 플레이리스트를 가져옴
        Playlist existingPlaylist = playlistService.getPlaylistById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // 파일 처리 로직 (파일이 있으면 처리, 없으면 기존 파일 경로 유지)
        MultipartFile file = playlistDto.getUserImgFile();
        String filePath = existingPlaylist.getUserImgPath(); // 기존 경로 유지

        if (file != null && !file.isEmpty()) {
            // 새로운 파일이 업로드되면 이미지 경로를 업데이트
            filePath = imageService.saveImage(file);
        }

        // 플레이리스트 수정 로직
        existingPlaylist.setPlaylistTitle(playlistDto.getPlaylistTitle());
        existingPlaylist.setContents(playlistDto.getContents());
        existingPlaylist.setUserImgPath(filePath); // 파일 경로를 업데이트

        // 플레이리스트 저장
        playlistRepository.save(existingPlaylist);

        return new ResponseEntity<>(existingPlaylist, HttpStatus.OK);
    }

    // 5. 플레이리스트 삭제
    @DeleteMapping
    public ResponseEntity<Void> deletePlaylists(@RequestBody List<String> playlistIds) {
        playlistService.deletePlaylists(playlistIds);
        return ResponseEntity.noContent().build(); // 삭제 후 204 No Content 응답
    }

    // 6. 플레이리스트에 음악을 추가
    // 플레이리스트에 음악을 추가하는 컨트롤러 메서드
    @PostMapping("/music")
    public ResponseEntity<ApiResponseDto> addMusicToPlaylists(
            @RequestBody PlayHistoryDto request) {
        // 플레이리스트에 음악 추가 로직 실행
        playlistService.addMusicToPlaylists(request);

        // 성공적으로 음악을 추가했음을 나타내는 ApiResponseDto 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto(true, "음악이 플레이리스트에 성공적으로 추가되었습니다."));
    }

    // 7. 플레이리스트에서 음악 삭제
}
