# Project : SNS
Tech Stack : JPA, QueryDSL
DBMS : MySQL, Redis
Server : Tomcat

Blog : https://velog.io/@ekxk1234  ,  https://blog.naver.com/ekxk1234

해야할일 : 로그인 시 출석체크, 최근 검색 기록 (친구 찾기)

로그인 시 or 쿠키로 로그인 처리시 -> redis hyperloglog에 {key: 오늘날짜} {value: 유저의 idx} insert

exception handler 수정 해야함