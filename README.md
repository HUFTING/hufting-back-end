# Team Logo
<p align='center'><img width=350 src='https://github.com/HUFTING/hufting-back-end/assets/67581495/e7179ae3-7365-4efb-8fdb-5cdafc3caf96'/></p>

# 1. 🧑🏻‍💻 Setup

```sh
# 0. clone fork project
git clone https://github.com/HUFTING/hufting-back-end.git

# 1. move target directory
cd hufting-back-end

# 2. build project
./gradlew build

# 3. move target directory & run server
cd build/libs
java -jar hufsting-0.0.1-SNAPSHOT.jar
```

# 2. OverView
## 2.1. Project Preview
<p align='center'><img src='https://github.com/HUFTING/hufting-back-end/assets/67581495/a5375a54-5ecf-4ace-aef0-ea777917bc99'/></p>

## 2.2. Project Architecture
<p align='center'><img src='https://github.com/HUFTING/hufting-back-end/assets/67581495/7493c8c7-6ef8-42a6-8a72-85c686c17409'/></p>

- **`Cloud DNS`**
    - https://www.hufting.com 으로 접속하였을 때 Public Subnet의 ws-server로 라우팅 하도록 함.
- **`Cloud NAT`**
    - NAT 게이트웨이를 열어 Public subnet, Private Subnet 내의 인스턴스가 외부와 통신할 수 있도록 함.
- **`Public Subnet`**
    - 퍼블릭 서브넷에 ws-server를 위한 Compute Engine 1대를 두었고, 해당 서버 내에 Nginx와 Next.js 서버를 띄움.
    - ws-server 사양
        - N1
        - vCpu 1개
        - Memory 3.75GB
- **`Private Subnet`**
    - 프라이빗 서브넷에 was-server를 위한 Compute engine 1대를 두었고, 해당 서버 내에 Spring 서버를 띄움.
- **`Cloud SQL`**
    - Cloud SQL을 이용하여 MySQL 데이터베이스를 1대 띄움.
    - 인스턴스 내에 데이터베이스 서버를 구축하지 않은 이유는 백업(Back-up), 모니터링(Monitoring)을 위해 관리형 데이터베이스를 이용함.

## 2.3. Internal Architecture
<p align='center'><img src='https://github.com/HUFTING/hufting-back-end/assets/67581495/2c542ac3-8235-4315-8409-c783aa6a62b0'/></p>

1. 클라이언트(Client)는 Nginx 서버로 접속한다. → 80 또는 **`443`**
    - Nginx 내부적으로 Certbot을 이용하여 SSL 인증을 진행
2. `Nginx`는 “/” 경로로 온 요청은 Next.js 컨테이너로 요청을 보낸다.
3. `Nginx`는 “/api” 경로로 온 요청은 Spring 컨테이너로 요청을 보낸다.
    - **BE 서버와 DB 서버는 Private Subnet에 구축⭐️**하여 사설 네트워크 망에서만 접근이 가능하도록 하였다.
    - 백엔드 컨테이너는 8080, 8081, 8082 포트로 3개의 컨테이너를 띄웠으며 앞단의 Nginx를 통해 라운드 로빈 방식으로 로드밸런싱한다.
4. 모든 요청과 응답은 Nginx를 통해 전달한다.
5. DB 서버는 GCP의 Cloud SQL을 이용하여 관리형 MySQL 서버를 구축하였다.

## 2.4. 🎨 Service Design
<img width="880" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/763dd0cc-62d0-4df6-baec-51f959ec09f1">

# 3. Service Detail
## **👥 훕팅 목록 확인, 생성 및 참여가 가능합니다.**
<p align='center'><img width="880" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/7bf47cac-e6f0-4fa7-8f62-cc65c6817ff0"></p>
<p align='center'><img width="880" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/578dc0e9-4c6c-42ee-ad24-ca89d0a65720"></p>

|Read|Create|Participate|
|---|---|---|
|<img width="389" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/15ae0e47-a4fc-41b0-82f4-19c60a7491c3">|<img width="381" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/0580285a-94e2-4e83-bb4a-6e895c259d94">|<img width="387" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/81c92696-61b0-42b3-a5f6-0745c3e047b7">|
|훕팅 목록 조회|훕팅 생성|훕팅 참가|

## **🔐 구글 소셜 로그인을 통해 로그인이 가능합니다.(단, hufs.ac.kr 도메인만 가능)
<p align='center'><img width="881" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/1be04934-775a-4bbf-b8a7-eba5d1bc7cef"></p>

|Login|Invlaid Account|
|---|---|
|<img width="260" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/7513b0d2-1ed2-4ba1-ab9d-dce855556876">|<img width="260" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/360587c7-4218-40c1-bb31-6e83d3367243">|
|훕팅 로그인 화면|학교 계정이 아닐 경우|

## **👨‍✈️ 내가 올린 훕팅글, 내가 요청한 훕팅 신청, 나에게 온 요청 모두 확인할 수 있습니다.
<p align='center'><img width="877" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/18aad145-a2b3-40c0-b7d5-46e5d2870e02"></p>

|Alarm|My Post|
|---|---|
|<img width="260" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/c3db9735-c4c5-471b-b122-7f7c6a0d00f9">|<img width="260" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/1fa6bbfc-9b09-4b56-a411-8e7fef0d33df">|
|알람 기능|내가 올린 훕팅 글|

## **👥 함께 참여할 친구와 팔로우할 수 있습니다.**
|My Mates|New Mate|
|---|---|
|<img width="260" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/c0b91900-edc3-4c65-adf6-1f83445b3c7d">|<img width="260" alt="image" src="https://github.com/HUFTING/hufting-back-end/assets/67581495/19f72d09-36bc-4272-80a6-4e0fe4c26711">|
|내 훕팅 메이트|훕팅 메이트 추가|
# 4. Tech stack

### Backend

<img alt="Django" src ="https://img.shields.io/badge/Django-092E20.svg?&style=for-the-badge&logo=Django&logoColor=white"/> <img alt="Python" src ="https://img.shields.io/badge/Python-3776AB.svg?&style=for-the-badge&logo=Python&logoColor=white"/>

### :computer: Contributor

| 설희관|
|:---:|
| Team Lead, BE |
|<img src="https://avatars.githubusercontent.com/u/67581495?v=4" height=100/>|
|[@SeolHuiGwan9478](https://github.com/SeolHuiGwan9478)|
