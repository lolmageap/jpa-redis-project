package cherhy.soloProject.application.domain.post.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public record PostRequestDto(
        @NotNull(message = "아이디는 필수") // 세션에서 가져올것
        Long memberId,
        @NotBlank(message = "제목을 입력해주세요")
        @Size(min = 5,message = "5자이상 적어주셔야합니다.")
        String title,
        @NotBlank(message = "내용을 입력해주세요")
        @Size(min = 5,message = "5자이상 적어주셔야합니다.")
        String content,
        List<String> photos
){}
