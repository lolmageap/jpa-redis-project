회원 = member_id, user_id, email, name, password, birthday, Address, basetime

회원로그 = memberHistory_id, member_id, member_name

게시물 = post_id, member_id(N), title, content, baseEntity

게시물 사진 = photo_id, post_id(N), photo

팔로우 = follow_id, follower(member_id(N)), follow(member_id(N)), basetime

//여기서부터 시작

댓글 = reply_id, post_id(N), member_id(N), content, basetime

타임라인 = timeline_id, member_Id(N), post_id, basetime

좋아요 = like_id, post_id, member_id, basetime

게시물 신고 = declare_id , member_id , post_id , basetime

게시물 차단 = postBlock_id, member_id, post_id

회원 차단 = memberBlock_id , member_id , block_user_id(member_id)

북마크 = bookmark_id, member_id, post_id, basetime

댓글 좋아요 = reply_like_id, reply_id , member_id

태그 = tag_id , member_id, tag_member_id

//나중에//
대댓글 = rereply_id, reply_id(N),
대댓글 좋아요 =
//나중에 인덱스 생성 쿼리 작성//