package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.CursorRequest;
import cherhy.soloProject.Util.PageCursor;
import cherhy.soloProject.application.domain.dto.PostDto;
import cherhy.soloProject.application.domain.dto.PostPhotoDto;
import cherhy.soloProject.application.service.postService.PostReadService;
import cherhy.soloProject.application.service.postService.PostWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시물")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostWriteService postWriteService;
    private final PostReadService postReadService;

    @Operation(summary = "게시물 생성")
    @PostMapping("/create")
    public void createPost(PostDto postDto){
        postWriteService.save(postDto);
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
    public PageCursor<PostPhotoDto> getPostScroll(@PathVariable Long member_id, CursorRequest cursorRequest){
        PageCursor<PostPhotoDto> postByMemberId = postReadService.findPostByMemberIdCursor(member_id, cursorRequest);
        return postByMemberId;
    }

}
