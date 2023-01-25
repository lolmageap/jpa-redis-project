package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.PageScroll;
import cherhy.soloProject.Util.ScrollRequest;
import cherhy.soloProject.application.domain.post.dto.PostDto;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.service.PostReadService;
import cherhy.soloProject.application.domain.post.service.PostWriteService;
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

    @Operation(summary = "게시물 수정")
    @PostMapping("/modify/{postId}")
    public void modifyPost(@PathVariable Long postId, PostDto postDto){
        postWriteService.modifyPost(postDto, postId);
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

}
