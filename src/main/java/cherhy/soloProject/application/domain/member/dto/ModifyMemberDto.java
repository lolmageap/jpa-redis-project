package cherhy.soloProject.application.domain.member.dto;

import javax.validation.constraints.Email;

public record ModifyMemberDto(
        Long id,
        String user_id,
        String name,
        @Email(message = "이메일 형식을 맞춰주세요")
        String email,
        String password
) {}
