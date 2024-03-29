## 프로젝트 소개

### Title : SNS

#### 개요
동시성 이슈를 중점적으로 생각하여 자주 사용하는 SNS의 기능들을 구현해보고 싶었습니다.

<hr>

### 🛠 Skill

#### Language
<p>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<br>
</p>

#### Tech Stack
<p>
<img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=JPA&logoColor=white"> <img src="https://img.shields.io/badge/QueryDSL-000000?style=for-the-badge&logo=QueryDSL&logoColor=white">
<br>
</p>

#### DataBase
<p>
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Redis-E34F26?style=for-the-badge&logo=Redis&logoColor=white">
<br>
</p>

#### Server 
<p>
<img src="https://img.shields.io/badge/Tomcat-FFCA28?style=for-the-badge&logo=Tomcat&logoColor=white">
<br>
</p>

#### OAS
<p>
<img src="https://img.shields.io/badge/Swagger Ui-01FF95?style=for-the-badge&logo=Swagger Ui&logoColor=white">
<br>
</p>

<hr>

### 기능

#### 로그인, 회원가입 🔐
- spring security를 이용한 social login과 로그인 시 출석 체크도 구현!!

#### 게시물 😄
- 회원은 게시물 등록이 가능하고 회원이 아니어도 게시물을 볼 수 있어요!

#### 팔로우 🤗
- 내가 좋아하는 회원은 팔로우를 해서 게시물이 타임라인에 나오게 할 수 있어요!

#### 타임라인 😲
- 팔로워들의 일상을 게시물을 통해 볼 수 있어요!

#### 좋아요 ❤
- 회원은 게시물에 좋아요를 남길 수 있어요. 좋아요를 누른 게시물은 나중에 확인이 가능하도록 만들었어요!

#### 댓글 🖊
- 회원은 자유롭게 게시물에 댓글을 달 수 있어요!

#### 검색 🔍
- 회원 이름을 검색할 수 있어요. 그리고 내가 최근에 검색한 정보들도 확인 할 수 있어요!

#### 게시물 차단 😤
- 보기 싫은 게시물은 차단할 수 있어요. 그러면 타임라인과 회원의 상세보기에 나오지 않아요. 차단한 게시물에서 조회도 가능해요!

#### 회원 차단 😡
- 보기 싫은 사람은 차단을 할 수 있어요. 팔로우한 사람을 차단하면 자연스레 팔로우도 끊기고 차단 목록에 들어가요!
- 차단당한 사람은 나의 게시물이 타임라인에 나오지않고 상세보기를 해도 내 게시물을 볼 수 없어요!
- 차단을 풀고 싶을 땐 차단 목록에서 차단 풀기를 할 수 있어요!

<hr>

### 이슈 및 트러블 슈팅 🔥

<details>
<summary><b>➡️ 좋아요 구현 트래픽 문제</b></summary>

> **문제** : 좋아요를 눌렀을 경우 RDBMS에 너무 잦은 부하와 동시성 처리 문제가 발생
>
> **설명** : 많은 사람들이 같은 게시물에 좋아요를 여러번 누르면 데이터베이스에 엄청나게 많은 부하가 몰리게 됩니다. 
> MySQL DBMS 같은 경우 Leader-follower 구조이기 때문에 조회를 할 수 있는 데이터베이스를 늘린 수는 있지만 Insert, Update, Delete와 같은 데이터를 변경 작업을 처리하는 Leader 데이터베이스는 늘릴 수 없습니다.
> 아무리 처리 시간이 짧은 Update 쿼리지만 동시성 처리까지 해야하며 정말 많은 데이터베이스에 부하는 곧 시스템 에러까지 발생할 수 있습니다.
>
> **해결** : Redis를 활용한 Increment, Decrement로 좋아요를 누를 시 게시물의 좋아요 정보를 레디스에 담습니다.
> 레디스는 싱글 스레드이기 때문에 동시성 문제가 발생하지 않고 좋아요 계산을 오차 없이 실행해줍니다.
> 그 이후 스케쥴러를 통해 바뀐 값을 데이터베이스에 bulk-update 해줍니다.
> 또한 레디스의 성능을 위해 Redis Scan을 통한 성능 최적화를 해주었습니다.
>
> **효과** : 좋아요와 좋아요 취소 요청이 많아져도 데이터베이스에 부하를 최소화해줬습니다.
> 동시에 여러 사람이 좋아요를 눌렀을 때에 발생하는 문제인 데이터 일관성, 데이터 적합성 이슈까지 모두 해결을 하였습니다. 
</details>

<details>
<summary><b>➡️ 잦은 동일한 데이터 조회 문제 </b></summary>

> **문제** : 같은 조회 요청을 다시 보냈을 경우의 문제
>
> **설명** : 사용자가 같은 데이터 조회를 다시 요청했을 때 DB에 접근을 다시 해야하는 문제가 발생하였습니다.
> 같은 데이터를 조회하는데 더 빠르고 데이터베이스에 부하가 가지 않게 해결을 하고 싶었습니다.
>
> **해결** : Redis와 Spring Framework에서 지원해주는 Cache 기능을 이용하여 캐싱 처리를 구현했습니다.
>
> **효과** : 데이터베이스의 부하는 줄었고 조회시 동일한 데이터 조회시 성능은 올라갔습니다.
</details>

<details>
<summary><b>➡️ 페이지네이션 문제 </b></summary>

> **문제** : 데이터를 조회할 때 불필요하게 많은 데이터를 조회하면서 조회시 성능에 문제가 발생
>
> **설명** : 데이터의 양이 많아 졌을 때 데이터를 전체 조회하는 것은 성능에 매우 큰 문제가 발생합니다.
> 기존 페이지 버튼을 누르면 넘어가는 방식의 페이징 기법은 프론트엔드 개발자 입장에서 구현이 매우 까다로우며 사용자 입장에서도 일일이 버튼을 눌러야 이동이 가능하기에 불편함이 있습니다.
>
> **해결** : 커스텀한 페이지네이션을 적용하여 사용자는 페이지버튼을 눌러 페이지 이동이 가능한 방식이 아닌 데이터를 조회하고 필요한 만큼 다시 스크롤을 내려 데이터를 추가적으로 조회하는 방식을 선택했습니다.
>
> **효과** : 성능은 기하급수적으로 올라갔으며 사용자는 보고싶은 만큼의 데이터를 확인 할 수 있고 버튼을 일일이 누르지 않아도 페이지를 넘길 수 있도록 편의성을 제공하였습니다.
</details>

<details>
<summary><b>➡️ 타임라인 문제 </b></summary>

> **문제** : Pull Model 방식으로 사용자가 타임라인을 조회할 때 게시물 테이블에서 조회를 하여 병목 현상이 발생 
>
> **설명** : 팔로우한 회원들이 게시물을 올리고 내 타임라인을 조회했을 때 팔로우한 회원의 게시물에서 연산을 통해 가져와야하는 문제가 발생합니다.
> 많은 게시물이 존재할 경우 병목현상이 발생하여 조회시 성능에 큰 이슈가 발생할 수 있습니다.
>
> **해결** : Push Model로 변환을 했습니다. 별도의 타임라인 테이블을 만들고 팔로우한 회원이 게시물을 등록하면 나의 타임라인에 등록이 됩니다.
>
> **효과** : 조회시 성능이 크게 올라갔습니다.
</details>

<hr>

## more info

<details>
<summary> DB ERD </summary>
<div markdown="1">

![ERD](https://user-images.githubusercontent.com/96738163/226175229-d8d543d4-b4b4-428d-9f5a-6667498cf2c8.jpg)
  
</div>
</details>




