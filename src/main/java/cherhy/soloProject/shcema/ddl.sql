회원 = member_id, user_id, email, name, password, birthday, Address, basetime

회원로그 = memberHistory_id, member_id, member_name

게시물 = post_id, member_id(N), title, content, baseEntity

게시물 사진 = photo_id, post_id(N), photo

팔로우 = follow_id, follower(member_id(N)), follow(member_id(N)), basetime

댓글 = reply_id, post_id(N), member_id(N), content, basetime

타임라인 = timeline_id, member_Id(following_member_id), post_id, basetime
    // push 아키텍쳐로 만드는 이유 내가 post를 작성하면 follower들이 바로 조회 할 수 있게 타임라인 테이블에 insert
    ex) kim follow cherhy -> cherhy write post -> timeline table insert (kim_id , cherhy_post_id)
        -> timeline table = timeLine_id, kim_id, cherhy_post_id -> kim이 sns 접속시 timeline table에서 kim_id만 있으면 바로 조회 가능

    // baseEntity에서 수정자를 넣어서 팔로워를 차단하면 수정자에 차단한 사람의 id가 들어가야됨 (나중에 구현)

//여기서부터 시작

좋아요 = like_id, post_id, member_id, basetime
    // 낙관적 락으로 구현 = 트래픽이 심하지 않을때 -> version 컬럼 추가 -> update table set version + 1 where version = :version
    -> 실패시 재귀함수 호출로 조회부터 다시 실행


게시물 신고 = declare_id , member_id , post_id , basetime

게시물 차단 = postBlock_id, member_id, post_id

게시물 조회수 = post_view_id, post_id, views
    redis 사용, set -> set변수명 : postViews + postId , key : userId , incr 1 , 집계 데이터 x초에 한번씩 rdb에 update query 날리면 됨

회원 차단 = memberBlock_id , member_id , block_user_id(member_id)

북마크 = bookmark_id, member_id, post_id, basetime

댓글 좋아요 방법 두개{
    요구사항 : 1. 좋아요를 누르면 그 댓글에 좋아요가 1 올라가고 한번 더 누르면 좋아요가 -1 그리고 좋아요가 사라짐
              2. 좋아요가 많은 게시물을 위주로 정렬 할 수 있어야하고 페이징 처리도 가능해야함
              3. 좋아요를 누른 게시자들을 확인 할 수 있어야함

댓글 좋아요 = reply_like_id, reply_id , member_id, post_id
    // 좋아요 순으로 페이징 : 좋아요 순으로 정렬 = where post_id = ?로 출력 로우 줄이고 and (group by reply_id로 그루핑)하고 count 순으로 정렬후 페이징
    // 카운트 쿼리, 즉 쿼리 연산이 많아 트래픽이 쏠릴때 매우 비효율적

댓글 좋아요 = reply_like_id, reply_id , member_id
    // 좋아요 순으로 페이징 : 댓글 좋아요 누를시
    // redis sorted set에 저장 후 score를 incr 1, 좋아요 취소시 score decr 1 (zincrby)
    // rdbms에 reply_id(1:1)와 member_id(1:N)만 저장해도 됨
    // 레디스가 날아가면 심각하기에 복제를 해두고 백업을 위한 로깅처리도 해야함
}

태그 = tag_id , member_id, tag_member_id

//나중에//
대댓글 = rereply_id, reply_id(N),
대댓글 좋아요 =
//나중에 인덱스 생성 쿼리 작성//