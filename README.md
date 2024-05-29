# micro-commerce
- MSA 기반 쇼핑몰 서버 개발 프로젝트 입니다.
- Java/Spring, MSA 학습용 사이드 프로젝트 특성 상 각 서비스에 해당하는 여러 모듈이 하나의 Github Repo 안에 같이 있도록 하였습니다.
- 외부 결제 시스템 연동의 어려움으로 쇼핑몰 내부에서 포인트를 충전해서 결제하는 방식으로 설계하였습니다.

<br/><br/>

# 🗂️ 목차
- [📑 시스템 설계](#-시스템-설계)
- [🖥 프로젝트 기술 스택](#-프로젝트-기술-스택)
- [📄 ERD](#-erd)

<br/><br/>

# 📑 시스템 설계
![image](https://github.com/f-lab-edu/micro-commerce/assets/66265199/28b81361-c161-4688-b906-2df186862e13)

- 조회 빈도가 높은 상품 데이터는 Redis 캐시를 적용하여 조회 성능 향상 및 DB 부하 감소
- 로직의 처리 시간이 길고 중요도가 높은 주문 서버 경우 Kafka를 사용한 이벤트 기반 비동기 처리로 대규모 트래픽에 대비
- 주문 완료, 결제 완료 등 특정 이벤트 마다 사용자에게 알림 전송을 위한 알림 서버도 Kafka를 활요하여 결합도 감소 및 확장성 증가
    - 추후 다른 알림도 쉽게 추가할 수 있도록 설계

<br/><br/>


# 🖥 프로젝트 기술 스택
- Java17
- SpringBoot 3.2.2
- Spring Cloud Gateway 4.1.0
- Spring Cloud Eureka 4.1.0
- Kafka
- Redis
- MySQL

<br/><br/>

# 📄 ERD
### 회원 서비스
![image](https://github.com/f-lab-edu/micro-commerce/assets/66265199/02d05429-1baa-4e98-b1e3-60b4dd5de93f)

<br>

### 결제 서비스
![image](https://github.com/f-lab-edu/micro-commerce/assets/66265199/b02ce0b5-71c4-4a85-898c-fae91bc47bc6)
- 포인트 사용 내역 저장 시 중복 문제를 방지하기 위해 트랜잭션 아이디(TX_ID) 컬럼 사용

<br>

### 상품 서비스
![image](https://github.com/f-lab-edu/micro-commerce/assets/66265199/1853e0a3-0126-4500-8aec-129f802fb9c9)
- 서비스간의 잦은 호출로 인한 오버헤드를 줄이기 위해 상품 정보 조회 시 자주 호출되는 판매자 정보를 같이 저장(반정규화)
- 상품:상품이미지 = 1:N 이므로, 별도 이미지 테이블을 구성하였고, 여러 이미지 중 노출 순서를 설정할 수 있도록 DISPLAY_ORDER 컬럼 사용

<br>

### 주문 서비스
![image](https://github.com/f-lab-edu/micro-commerce/assets/66265199/7ac7585f-a0be-4324-aca1-37169c65cbb4)
- 한 주문의 여러 상품을 주문할 수 있으므로, "주문 기본 정보" 테이블과 "주문 상품 정보" 테이블 분리
- 주문 상품 정보, 장바구니 데이터 저장 시 서비스간의 잦은 호출로 인한 오버헤드를 줄이기 위해 상품 정보(이름, 가격, 이미지 등) 포함(반정규화)

