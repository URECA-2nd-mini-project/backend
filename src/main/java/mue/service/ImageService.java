package mue.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final String uploadDir = "mue_uploads/image"; // 저장할 디렉토리 경로
   // 이미지 저장 메소드
   public String saveImage(MultipartFile image) {
    try {
        // uploads 디렉토리 생성
        File directory = new File(uploadDir); //file객체 생성
        if (!directory.exists()) { // 존재여부 확인
            directory.mkdirs(); // (mkdirs() : 필요한 모든 디렉토리를 ("mue_uploads/image")생성 )
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename(); // 중복 방지 파일명 생성
        File file = new File(uploadDir, fileName); // 파일 객체 생성
        image.transferTo(file); // 유저 이미지 저장 

        return "/mue_uploads/image/"  + fileName; // 저장된 파일의 URL 반환
    } catch (IOException e) {
        throw new RuntimeException("Failed to save image: " + e.getMessage()); // 예외 처리
    }
}

    // 이미지 수정 메소드
    public String updateImage(String userImg, MultipartFile newImage) {
        // 기존 이미지 삭제
        deleteImage(userImg); // userImg로 기존 이미지 파일 삭제

        // 새 이미지 저장
        return saveImage(newImage); // 새 이미지 저장 후 URL 반환
    }

    // 이미지 삭제 메소드
    public void deleteImage(String userImg) {
        try {
            // userImg에서 /uploads/ 부분을 제거하여 실제 경로를 생성
            Path path = Paths.get(uploadDir + userImg.replace("/uploads/", ""));
            Files.deleteIfExists(path); // 파일 삭제
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + e.getMessage()); // 예외 처리
        }
    }
    }

