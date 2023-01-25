package cherhy.soloProject.application.domain.member.entity;

import cherhy.soloProject.Util.BaseTimeEntity;
import cherhy.soloProject.application.domain.follow.entity.Follow;
import cherhy.soloProject.application.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.isTrue;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(unique = true)
    private String user_id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String name;
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    //member -> follow 단방향 참조
    @OneToMany(mappedBy = "follower")
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    private List<Follow> follows = new ArrayList<>();

    @Builder //회원가입
    public Member(String user_id, String name, String email, String password, LocalDate birthday) {
        validUserId(user_id);
        this.user_id = requireNonNull(user_id);
        validName(name);
        this.name = requireNonNull(name);
        validPwd(name);
        this.password = requireNonNull(password);
        this.email = requireNonNull(email);
    }

    public void validName(String name) {
        isTrue(name.length() <= 10, "이름은 10글자를 넘을 수 없습니다.");
    }

    public void validUserId(String user_id) {
        System.out.println("user_id = " + user_id);
        isTrue(user_id.length() >= 5, "아이디는 5글자 이상이어야합니다.");
    }

    public void validPwd(String password) {
        isTrue(password.length() >= 5, "비밀번호는 5글자 이상입니다.");
    }

    public void changeName(String changeName) {
        this.name = changeName;
    }

    public void changeUserId(String changeUserId) {
        this.user_id = changeUserId;
    }

    public void changePassword(String changePassword) {
        this.password = changePassword;
    }


}
