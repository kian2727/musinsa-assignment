
## 구현범위
## 코드 빌드 
## 코드 테스트
## 실행방법
## etc
- H2 설정
1. `db/h2/bin` 에있는 `h2.sh` 실행 (운영환경에따라서)
2. 실행 후 자동으로 열리는 창에서 ip를 localhost로 변경후 접속, 이후 `JDBC URL` 값을 `jdbc:h2:~/musinsa` 변경후 connect, root
![img.png](doc/img/img.png)
홈 디렉토리에서 `musinsa.mv.db` 파일 생성 확인
3. 이후 동일하게 `h2.sh` 실행해서 `JDBC URL` 값을 `jdbc:h2:tcp://localhost/~/musinsa` 변경후 접속
