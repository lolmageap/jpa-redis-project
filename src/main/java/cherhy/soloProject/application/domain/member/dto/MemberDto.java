package cherhy.soloProject.application.domain.member.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


public record MemberDto(
        @NotBlank(message = "id를 입력해주세요") String user_id,
        @NotBlank(message = "이름을 입력해주세요") String name,
        @NotBlank(message = "이메일을 입력해주세요")
        @Email(message = "이메일 형식을 맞춰주세요")
        String email,
        @NotBlank(message = "비밀번호를 입력해주세요") String password
//       , LocalDate birthday
) {}
