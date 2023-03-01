package cherhy.soloProject.domain.member.dto.request;

import javax.validation.constraints.NotBlank;

public record SignInRequestDto(
        @NotBlank(message = "아이디는 필수입니다") String userId,
        @NotBlank(message = "비밀번호는 필수입니다") String password
) {
}
