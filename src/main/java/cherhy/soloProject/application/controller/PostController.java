package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.usecase.MemberTimeLineUseCase;
import cherhy.soloProject.application.utilService.SessionReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "게시물")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final MemberTimeLineUseCase memberPostUseCase;
    private final SessionReadService sessionReadService;

    @Operation(summary = "게시물 생성 // Push Model")
    @PostMapping("/create")
    public ResponseEntity createPost(@RequestBody @Valid PostRequestDto postRequestDto){
        return memberPostUseCase.createPost(postRequestDto);
    }

    @Operation(summary = "게시물 수정")
    @PutMapping("/modify/{postId}")
    public ResponseEntity modifyPost(@RequestBody @Valid PostRequestDto postRequestDto, @PathVariable Long postId){
        return memberPostUseCase.modifyPost(postRequestDto, postId);
    }

    @Operation(summary = "사용자의 게시물 불러오기, 전체 // 로그인 했으면 차단된 게시물 제외")
    @GetMapping("/{memberId}")
    public List<PostPhotoDto> getPost(@PathVariable("memberId") Long memberId, HttpSession session){
        Long memberSessionId = sessionReadService.getUserDataNoThrow(session);
        if (memberSessionId == null){
            return memberPostUseCase.findPostByMemberId(memberId);
        }
        return memberPostUseCase.findPostByMemberId(memberId,memberSessionId);
    }

    @Operation(summary = "사용자의 게시물 불러오기, 페이징 // 로그인 했으면 차단된 게시물 제외")
    @GetMapping("/{memberId}/page")
    public Page<PostPhotoDto> getPostPage(@PathVariable Long memberId, Pageable pageable, HttpSession session){
        Long memberSessionId = sessionReadService.getUserDataNoThrow(session);
        if (memberSessionId == null){
            return memberPostUseCase.findPostByMemberIdPage(memberId, pageable);
        }
        return memberPostUseCase.findPostByMemberIdPage(memberId,memberSessionId, pageable);

    }

    @Operation(summary = "사용자의 게시물 불러오기 , 무한 스크롤 // 로그인 했으면 차단된 게시물 제외")
    @GetMapping("/{member_id}/scroll")
    public ScrollResponse<PostPhotoDto> getPostScroll(@PathVariable Long member_id, ScrollRequest scrollRequest, HttpSession session){
        Long memberSessionId = sessionReadService.getUserDataNoThrow(session);
        if (memberSessionId == null){
            return memberPostUseCase.findPostByMemberIdCursor(member_id, scrollRequest);
        }
        return memberPostUseCase.findPostByMemberIdCursor(member_id, memberSessionId, scrollRequest);
    }

}
