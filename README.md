# Project : SNS
Tech Stack : JPA, QueryDSL
DBMS : MySQL, Redis
Server : Tomcat

Blog : https://velog.io/@ekxk1234  ,  https://blog.naver.com/ekxk1234

해야할일 : 좋아요 구현 및 스케줄러 활용, 로그인 시 출석체크, 최근 검색 기록 (친구 찾기)

좋아요 시 PostLike 테이블에 member_id, post_id 저장 ( 누가 어떤 게시물을 좋아요 눌렀는지 )
RedisStream으로 스케줄러가 돌 때 PostLike 테이블에 insert
insert 후 그 이후 데이터들 쌓기 (event-driven)

개발 환경 : 도커로 1개의 레디스 서버로 테스트 환경 구축


좋아요 -> redis increase or decrease -> postLike Table insert or delete -> scheduler update

exception handler 수정 해야함