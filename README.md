# Project : SNS
Tech Stack : JPA, QueryDSL
DBMS : MySQL, Redis
Server : Tomcat

Blog : https://velog.io/@ekxk1234  ,  https://blog.naver.com/ekxk1234

해야할일 : 로그인 시 출석체크, 최근 검색 기록 (친구 찾기)


usecase와 service로 나눠서 메인로직은 usecase 비즈니스 로직은 서비스에서 실행

로그인, 회원가입 -> spring security를 이용한 social login

redis -> 클러스터 구조로 구현 -> aof파일로 백업을 하는 방식

Exception 처리 -> Exception Handler를 이용한 에러 핸들링

기본 페이징 처리 -> 커스텀한 scrollDto로 무한 스크롤 구현, 일부 pageable을 이용하기도 함

댓글 등록 -> 댓글 정렬 방법 두가지 구현
1. redis에 Sorted Set을 이용한 날짜순 정렬 구현 (추가와 변화에 대응하기 좋음, 캐싱과 같은 효과를 봄 [검색이 매우 빠름] , 단점 : cluster 구조로 구현을 했지만 휘발될 위험이 있음)
   - redis에 접근 -> size 만큼 key값 조회 -> 조회된 댓글 id를 가지고 댓글 조회 -> redis를 사용한 covering index
2. RDB의 modifiedDate를 키로 삼아 정렬 (modifiedDate는 자주 update ==> 인덱스로 사용하기 부적절 and 코드에 연산이 많이 들어가 성능이 비교적 좋지않고 구현이 어려움)
3. 데이터가 휘발되어 redis에서 키값 조회가 안되면 RDB로 접근하는 방식

게시물 등록 시 -> 게시글 table insert -> photo table insert -> 팔로워들 timeline insert (Push Model) -> 타임라인을 만들었기에 쓰기 성능 ↓, 조회 성능 ↑

좋아요 , 좋아요 취소 시 -> 좋아요 테이블에 insert (누가 좋아요를 눌렀는지 확인하기 위한 테이블) -> RDB에 부하를 줄이기 위해 redis에 increase, decrease 후 scheduler로 update -> 동시성 처리 해결 및 redis scan을 통한 성능 최적화

좋아요 테이블을 따로 만들어서 누가 어떤 게시물에 좋아요를 눌렀는지 확인 가능

로그인 시 or 쿠키로 로그인 처리시 -> redis bitmap에 {key: 오늘날짜} {value: 유저의 idx} insert
