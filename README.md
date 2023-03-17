# Project : SNS
Tech Stack : JPA, QueryDSL
DBMS : MySQL, Redis
Server : Tomcat

Blog : https://velog.io/@ekxk1234  ,  https://blog.naver.com/ekxk1234

해야할일 : 최근 검색 기록 (친구 찾기) , 캐싱 활용 , usecase와 service로 나눠서 메인로직은 usecase 비즈니스 로직은 서비스에서 실행

로그인, 회원가입 -> spring security를 이용한 social login

redis -> 클러스터 구조로 구현 -> aof파일로 백업을 하는 방식

Exception 처리 -> Exception Handler를 이용한 에러 핸들링

기본 페이징 처리 -> 커스텀한 scrollDto로 무한 스크롤 구현, 일부 pageable을 이용하기도 함

댓글 등록 -> 댓글 정렬 방법 두가지 구현
1. redis에 Sorted Set을 이용한 날짜순 정렬 구현 (추가와 변화에 대응하기 좋음, 캐싱과 같은 효과를 봄 [검색이 매우 빠름] , 단점 : cluster 구조로 구현을 했지만 휘발될 위험이 있음)
->  redis에 접근 -> size 만큼 key값 조회 -> 조회된 댓글 id를 가지고 댓글 조회 -> redis를 사용한 covering index
   
2. RDB의 modifiedDate를 키로 삼아 정렬 (modifiedDate는 자주 update ==> 인덱스로 사용하기 부적절 and 코드에 연산이 많이 들어가 성능이 비교적 좋지않고 구현이 어려움)

3. 데이터가 휘발되어 redis에서 키값 조회가 안되면 RDB로 접근하는 방식

게시물 등록 시 -> 게시글 table insert -> photo table insert -> 팔로워들 timeline insert (Push Model) -> 타임라인을 만들었기에 쓰기 성능 ↓, 조회 성능 ↑

좋아요 , 좋아요 취소 시 -> 좋아요 테이블에 insert (누가 좋아요를 눌렀는지 확인하기 위한 테이블)
-> RDB에 부하를 줄이기 위해 redis에 increase, decrease 후 scheduler로 update 
-> 동시성 처리 해결 및 redis scan을 통한 성능 최적화

좋아요 테이블을 따로 만들어서 누가 어떤 게시물에 좋아요를 눌렀는지 확인 가능

로그인 시 or 쿠키로 로그인 처리시 -> redis bitmap에 ({key: 오늘날짜}, {value: 유저의 idx}, 1) insert - 출석체크

최근 검색 기록(이름)

게시물 차단 -> 게시물을 차단하면 타임라인과 상세보기에서 볼 수 없고 차단한 게시물에서 조회 가능

사람 차단 -> 사람을 차단하면 차단당한 사람은 차단한 사람의 타임라인과 상세보기에서 볼 수 없다. 메세지도 보낼 수 없음(메세지는 미구현)
- 사람을 차단하면 timeline 게시물의 데이터를 삭제? (삭제 아직 안함)
- 팔로우를 끊고 조회시 가져오지만 않게? (지금은 이렇게 구현)


## 참고 
google : 검색창을 처음 눌렀을 때 -> 내가 검색하려는 단어의 조합 'Like%' -> 검색어의 순위 5위까지
facebook : 검색창 페이지를 들어갔을 때 최근 검색 데이터 5개 가져옴


## 구현 예정

연관 검색어 -> ? -> 검색을 하고 바로 다음에 검색한 데이터 +1 -> 일정 점수 이상 or 상위 5개 ?

게시물 리스트를 볼 때 내 팔로워중에 누가 나의 게시물을 좋아요 눌렀는지 한명만 상단에 표시 , 내 팔로워 중에 좋아요 누른 사람이 없으면 null

지금, 오늘 좋아요 순위가 가장 높은 게시물 확인 

오늘 가장 많이 검색된 순위 , 실시간 인기 검색

조회 최근 검색어 5개만, Write-Through pattern으로 캐싱처리

내 팔로워들이나 팔로우하는 사람들이 가장 즐겨 검색하는 단어 or 게시물 top 5
-> 내가 검색을 한 데이터는 스케줄러를 통해 다시 DB에 History Table에 bulk insert 되어야 한다.
-> history table -> idx, memberId, searchName, createAt
