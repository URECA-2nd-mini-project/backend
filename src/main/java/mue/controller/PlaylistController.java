package mue.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mue.service.PlaylistService;
import mue.entity.Playlist;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;


    // 사용자 ID로 모든 플레이리스트 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Playlist>> getAllPlaylistsByUserId(@PathVariable String userId) {
        List<Playlist> playlists = playlistService.getAllPlaylistsByUserId(userId);
        return new ResponseEntity<>(playlists, HttpStatus.OK);
    }

    // 플레이리스트 ID로 세부정보 조회
    @GetMapping("/{playlistId}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable String playlistId) {
        Playlist playlist = playlistService.getPlaylistById(playlistId)
                                           .orElseThrow(() -> new RuntimeException("Playlist not found"));
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }
    // 특정 감정 태그를 가진 플레이리스트 조회
    @GetMapping("/user/{userId}/emotion/{emotionTag}")
    public ResponseEntity<List<Playlist>> getPlaylistsByUserIdAndEmotionTag(@PathVariable String userId, 
                                                                             @PathVariable String emotionTag) {
        List<Playlist> playlists = playlistService.getPlaylistsByUserIdAndEmotionTag(userId, emotionTag);
        return new ResponseEntity<>(playlists, HttpStatus.OK);
    }

    // 플레이리스트 생성
    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Playlist playlist) {
        Playlist createdPlaylist = playlistService.createPlaylist(playlist);
        return new ResponseEntity<>(createdPlaylist, HttpStatus.CREATED);
    }

    // 플레이리스트 수정
    @PutMapping("/{playlistId}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable String playlistId, 
                                                   @RequestBody Playlist updatedPlaylist) {
        Playlist playlist = playlistService.updatePlaylist(playlistId, updatedPlaylist);
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    // 플레이리스트 삭제
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable String playlistId) {
        return playlistService.deletePlaylist(playlistId);
    }
}
