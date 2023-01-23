package cherhy.soloProject.application.domain.entity;

import cherhy.soloProject.Util.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(unique = true)
    private String user_id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String name;
    private String password;
    private LocalDate birthday;
    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    //member -> follow 단방향 참조
    @OneToMany
    @JoinColumn(name = "follower")
    private List<Follow> followers = new ArrayList<>(); //joinColumn 문제 생길거같은데...

    @OneToMany
    @JoinColumn(name = "follow")
    private List<Follow> follows = new ArrayList<>(); //joinColumn 문제 생길거같은데...

//    @OneToMany(mappedBy = "member")
//    private List<TimeLine> timeLines = new ArrayList<>();

    @Builder //회원가입
    public Member(String user_id, String name, String email, String password, LocalDate birthday) {
        this.user_id = requireNonNull(user_id);
        validName(name);
        this.name = requireNonNull(name);
        this.email = requireNonNull(email);
        this.password = requireNonNull(password);
        this.birthday = birthday;
    }

    private void validName(String name) {
        isTrue(name.length() <= 10, "최대 길이를 초과했습니다.");
    }

    private void ChangeName(String changeName) {
        this.name = changeName;
    }

    private void changeUserId(String changeUserId) {
        this.name = changeUserId;
    }

    private void changePassword(String changePassword) {
        this.name = changePassword;
    }


}
