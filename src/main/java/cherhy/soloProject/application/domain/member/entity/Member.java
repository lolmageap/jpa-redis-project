package cherhy.soloProject.application.domain.member.entity;

import cherhy.soloProject.Util.BaseEntity;

import cherhy.soloProject.Util.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue
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

    @Builder
    public Member(String user_id, String name, String email, String password, LocalDate birthday) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
    }

}
