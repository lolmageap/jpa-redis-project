# Project : SNS
Tech Stack : JPA, QueryDSL
DBMS : MySQL, Redis
Server : Tomcat

Blog : https://velog.io/@ekxk1234  ,  https://blog.naver.com/ekxk1234

해야할일 : 좋아요 구현 및 스케줄러 활용, 로그인 시 출석체크, 최근 검색 기록 (친구 찾기)

개발 환경 : 도커로 1개의 레디스 서버로 테스트 환경 구축

// 게시물 조회 로직
// 좋아요 테이블 조회
// 좋아요를 해놓은 데이터가 없으면 좋아요 테이블에 insert
// 레디스에 조회 테이블 id 값과 조회수 increment
// 좋아요를 해놨었다면 데이터를 delete
// 레디스에 조회 테이블 id 값과 조회수 decrement