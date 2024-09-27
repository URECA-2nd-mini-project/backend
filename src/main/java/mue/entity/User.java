package mue.entity;

import mue.entity.BaseTimeEntity;
import mue.enums.Role; // Role enum 추가

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // Getter 생성
@NoArgsConstructor // Default 생성자
@Entity // Entity임을 명시
@Table(name = "user") // 테이블 이름을 소문자로 설정
public class User extends BaseTimeEntity { // BaseTimeEntity 상속
    
    @Id // Primary Key
    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;
    
    @Column(name = "name", nullable = false, length = 100) // name 필드 추가
    private String name; // name 필드 추가

    @Column(name = "gmail", nullable = false, length = 255)
    private String gmail; 
    
    @Column(name = "photo_url", length = 512)
    private String photoUrl;
    
    @Enumerated(EnumType.STRING) // Enum 값 저장
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String userId, String name, String gmail, String photoUrl, Role role) { // 'gmail' 사용
        this.userId = userId;
        this.name = name;
        this.gmail = gmail; // 여기서 'gmail'을 초기화
        this.photoUrl = photoUrl;
        this.role = role;
    }

    // update 함수 구현
    public User update(String gmail, String photoUrl) {
        this.gmail = gmail;
        this.photoUrl = photoUrl;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}