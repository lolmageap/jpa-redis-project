package cherhy.soloProject.domain.member.dto.request;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
public record MemberRequest(
        @NotBlank(message = "id를 입력해주세요")
        @Size(min = 5, message = "아이디는 5글자 이상이어야합니다.")
        String user_id,
        @NotBlank(message = "이름을 입력해주세요")
        @Size(max = 10, message = "이름은 10글자를 넘을 수 없습니다.")
        String name,
        @NotBlank(message = "이메일을 입력해주세요")
        @Email(message = "이메일 형식을 맞춰주세요")
        String email,
        @Size(min = 5, message = "비밀번호는 5글자 이상입니다.")
        @NotBlank(message = "비밀번호를 입력해주세요") String password
) {}