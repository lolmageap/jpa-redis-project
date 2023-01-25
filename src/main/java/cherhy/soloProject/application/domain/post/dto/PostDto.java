package cherhy.soloProject.application.domain.post.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

public record PostDto(
        @NotBlank(message = "세션 만료") Long memberId,
        @NotBlank(message = "제목을 입력해주세요") String title,
        @NotBlank(message = "내용을 입력해주세요") String content,
        List<String> photos
){}
