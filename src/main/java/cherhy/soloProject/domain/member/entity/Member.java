package cherhy.soloProject.domain.member.entity;

import cherhy.soloProject.Util.BaseTimeEntity;
import cherhy.soloProject.domain.follow.entity.Follow;
import cherhy.soloProject.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.postBlock.entity.PostBlock;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER",
        indexes = @Index(name = "user_id__index__member", columnList = "user_id"))

public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(unique = true, name = "user_id")
    private String userId;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private List<Follow> follows = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PostBlock> postBlocks = new ArrayList<>();

    @OneToMany(mappedBy = "blockMember", cascade = CascadeType.ALL)
    private List<MemberBlock> memberBlocks = new ArrayList<>();

    @Builder //회원가입
    private Member(String userId, String name, String email, String password) {
        this.userId = requireNonNull(userId);
        this.name = requireNonNull(name);
        this.password = requireNonNull(password);
        this.email = requireNonNull(email);
    }

    public void changeName(String changeName) {
        this.name = changeName;
    }

    public void changeUserId(String changeUserId) {
        this.userId = changeUserId;
    }

    public void changePassword(String changePassword) {
        this.password = changePassword;
    }

}
