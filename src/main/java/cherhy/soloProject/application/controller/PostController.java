package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.service.PostReadService;
import cherhy.soloProject.application.domain.post.service.PostWriteService;
import cherhy.soloProject.application.domain.postLike.dto.PostLikeDto;
import cherhy.soloProject.application.domain.postLike.service.PostLikeReadService;
import cherhy.soloProject.application.domain.postLike.service.PostLikeWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "게시물")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostWriteService postWriteService;
    private final PostReadService postReadService;

    private final PostLikeReadService postLikeReadService;

    private final PostLikeWriteService postLikeWriteService;

    @Operation(summary = "게시물 생성 // Push Model")
    @PostMapping("/create")
    public String createPost(@RequestBody @Valid PostRequestDto postRequestDto){
        return postWriteService.createPost(postRequestDto);
    }

    @Operation(summary = "게시물 수정")
    @PutMapping("/modify/{postId}")
    public String modifyPost(@RequestBody @Valid PostRequestDto postRequestDto, @PathVariable Long postId){
        return postWriteService.modifyPost(postRequestDto, postId);
    }

    @Operation(summary = "사용자의 게시물 불러오기, 전체")
    @GetMapping("/{member_id}")
    public List<PostPhotoDto> getPost(@PathVariable Long member_id){
        List<PostPhotoDto> postByMemberId = postReadService.findPostByMemberId(member_id);
        return postByMemberId;
    }

    @Operation(summary = "사용자의 게시물 불러오기, 페이징")
    @GetMapping("/{member_id}/page")
    public Page<PostPhotoDto> getPostPage(@PathVariable Long member_id, Pageable pageable){
        Page<PostPhotoDto> postByMemberId = postReadService.findPostByMemberIdPage(member_id, pageable);
        return postByMemberId;
    }

    @Operation(summary = "사용자의 게시물 불러오기 , 무한 스크롤")
    @GetMapping("/{member_id}/scroll")
    public PageScroll<PostPhotoDto> getPostScroll(@PathVariable Long member_id, ScrollRequest scrollRequest){
        PageScroll<PostPhotoDto> postByMemberId = postReadService.findPostByMemberIdCursor(member_id, scrollRequest);
        return postByMemberId;
    }

    @Operation(summary = "타임라인 조회 // only RDBMS")
    @GetMapping("/timeLine/{member_id}")
    public PageScroll<PostPhotoDto> getTimeLine(@PathVariable Long member_id, ScrollRequest scrollRequest){
        PageScroll<PostPhotoDto> timeLine = postReadService.getTimeLine(member_id, scrollRequest);
        return timeLine;
    }

    @Operation(summary = "좋아요, 취소")
    @PostMapping("postLike")
    public String postLike(@Valid @RequestBody PostLikeDto postLikeDto){
        String result = postLikeWriteService.postLike(postLikeDto);
        return result;
    }

    @Operation(summary = "breakpoint 좋아요 동시성 테스트")
    @PostMapping("postLike/example")
    public String postExample(@Valid @RequestBody PostLikeDto postLikeDto){
        String result = postLikeWriteService.postExample(postLikeDto);
        return result;
    }

}
