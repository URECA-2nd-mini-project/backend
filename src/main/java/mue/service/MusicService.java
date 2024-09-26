package mue.service;

import mue.entity.*;
import mue.repository.MusicRepository;
import mue.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicService {

  private final MusicRepository musicRepository;

  @Autowired
  public MusicService(PlaylistRepository playlistRepository, MusicRepository musicRepository) {
    this.musicRepository = musicRepository;
  }
}