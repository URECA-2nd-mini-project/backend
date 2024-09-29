package mue.dto;

// 로그인된 사용자의 정보를 세션에 저장하고 사용할 수 있도록 하는 기능
// 매번 DB에서 사용자 정보를 불러오는 대신, 세션에서 빠르게 사용자 정보를 가져올 수 있도록 기능 구현 필요
import lombok.Getter;
import mue.entity.User;
import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String userId;
    private String name;
    private String gmail; // email을 gmail로 수정
    private String photoUrl; // picture를 photoUrl로 수정

    public SessionUser(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.gmail = user.getGmail(); // email -> gmail
        this.photoUrl = user.getPhotoUrl(); // picture -> photoUrl
    }
}
