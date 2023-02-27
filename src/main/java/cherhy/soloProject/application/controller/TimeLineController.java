package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.usecase.MemberTimeLineUseCase;
import cherhy.soloProject.application.utilService.SessionReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Tag(name = "타임라인")
@RestController
@RequiredArgsConstructor
@RequestMapping("/timeLine")
public class TimeLineController {

    private final SessionReadService sessionReadService;
    private final MemberTimeLineUseCase memberPostTimeLineUseCase;

    @Operation(summary = "타임라인 조회 // only RDBMS")
    @GetMapping
    public ScrollResponse<PostPhotoDto> getTimeLine(ScrollRequest scrollRequest, HttpSession session){
        Member userData = sessionReadService.getUserData(session);
        return memberPostTimeLineUseCase.getTimeLine(userData.getId(), scrollRequest);
    }

}
