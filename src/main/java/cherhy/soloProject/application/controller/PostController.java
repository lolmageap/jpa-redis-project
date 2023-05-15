package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.application.usecase.TimeLineUseCase;
import cherhy.soloProject.application.utilService.SessionReadService;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.domain.post.dto.request.PostRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Tag(name = "게시물")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final TimeLineUseCase memberPostUseCase;
    private final SessionReadService sessionReadService;

    @Operation(summary = "게시물 생성 // Push Model")
    @PostMapping
    public ResponseEntity createPost(@RequestBody @Valid PostRequestDto postRequestDto, Principal principal){
        Member member = sessionReadService.getUserData(principal);
        return memberPostUseCase.createPost(postRequestDto, member);
    }

    @Operation(summary = "게시물 수정")
    @PutMapping("/{postId}")
    public ResponseEntity modifyPost(@RequestBody @Valid PostRequestDto postRequestDto,
                                     @PathVariable Long postId, Principal principal){
        Member member = sessionReadService.getUserData(principal);
        return memberPostUseCase.modifyPost(postRequestDto, member, postId);
    }

    @Operation(summary = "사용자의 게시물 불러오기, 전체 // 로그인 했으면 차단된 게시물 제외")
    @GetMapping("/{memberId}")
    public List<PostPhotoDto> getPost(@PathVariable("memberId") Long memberId, Principal principal){
        Member member = sessionReadService.getUserDataNoThrow(principal);
        if (member == null){
            return memberPostUseCase.findPostByMemberId(memberId);
        }
        return memberPostUseCase.findPostByMemberId(memberId,member);
    }

    @Operation(summary = "사용자의 게시물 불러오기, 페이징 // 로그인 했으면 차단된 게시물 제외")
    @GetMapping("/{memberId}/page")
    public Page<PostPhotoDto> getPostPage(@PathVariable Long memberId, Pageable pageable, Principal principal){
        Member member = sessionReadService.getUserDataNoThrow(principal);
        if (member == null){
            return memberPostUseCase.findPostByMemberIdPage(memberId, pageable);
        }

        return memberPostUseCase.findPostByMemberIdPage(memberId,member, pageable);
    }

    @Operation(summary = "사용자의 게시물 불러오기 , 무한 스크롤 // 로그인 했으면 차단된 게시물 제외")
    @GetMapping("/{memberId}/scroll")
    public ScrollResponse<PostPhotoDto> getPostScroll(@PathVariable Long memberId, ScrollRequest scrollRequest, Principal principal){
        Member member = sessionReadService.getUserDataNoThrow(principal);
        if (member == null){
            return memberPostUseCase.findPostByMemberIdCursor(memberId, scrollRequest);
        }
        return memberPostUseCase.findPostByMemberIdCursor(memberId, member, scrollRequest);
    }

//    @Operation(summary = "추천 게시물")
//    @GetMapping("/{memberId}")
//    public ScrollResponse<PostPhotoDto> hi(ScrollRequest scrollRequest){
//        Long memberSessionId = sessionReadService.getUserData(session);
//        memberPostUseCase.getFeaturedPosts(memberSessionId,scrollRequest);
//        return ;
//    }

}
