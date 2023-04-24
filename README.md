# Testcontainers

## Redis

### embedded redis(it.ozimov:embedded-redis)

- 장점
    - 의존성 추가만으로 사용 가능
- 단점
    - 스프링 컨텍스트 당 하나 씩 실행 됨 -> 포트 충돌
    - [m1 에서 생하는 문제](https://da-nyee.github.io/posts/how-to-use-embedded-redis-on-m1-arm/)
        - 직접 binary 파일 지정하여 RedisServer 생성
    - 버전 관리 더 이상 안되는 중
    - [Embedded Redis 를 쓰면서 겪은 문제와 해결방안](https://rogal.tistory.com/entry/Embedded-Redis-%EB%A5%BC-%EC%93%B0%EB%A9%B4%EC%84%9C-%EA%B2%AA%EC%9D%80-%EB%AC%B8%EC%A0%9C%EC%99%80-%ED%95%B4%EA%B2%B0%EB%B0%A9%EC%95%88)
        - 의존성 문제 : slf4j logger 중복 구현으로 인한 컴파일 에러(in. Embedded Redis 0.7.3버전)
          ->  `implementation ('it.ozimov:embedded-redis:0.7.3') { exclude group: "org.slf4j", module: "slf4j-simple" }
          `
        - 메모리 관련 런타임 에러
            - `--maxheap`, `--maxmemory` 수기 설정 해야함
        - 스프링 비정상 종료 시 레디스 서버 안꺼지는 현상 -> try / catch 를 통해 에러 무시(안꺼진 레디스 사용)

### Testcontainers 사용

- 장점
    - 테스트 후 컨테이너 자동 종료
    - 포트 겹칠 일 없음
- 단점
    - 테스트 실행 환경에서 도커가 깔려있어야 함
    - 컨테이너 뜨는 속도 때문에 테스트 실행 속도가 느림

## Trouble Shooting

###                      

- 현상
    - @Testcontainers 테스트 실행 시 `Could not find a valid Docker environment. Please see logs and check configuration`
- 환경
    - Mac(intel)
    - IntelliJ IDE
    - Rancher Desktop
- 원인
    - docker socket 을 찾지 못함
        - 실제로 일반적인 도커 소켓 파일의 위치인 `/var/run/docker.sock`에 소켓 파일이 비어있음
        - Docker Desktop 의 예전 버전의 경우 `/var/run/docker.sock`가 존재하지 않아 수동으로 심볼릭 링크를 추가해주어야 했음
            - 최신 버전은 자동으로 해줌
            - Rancher Desktop 도 Docker Desktop을 기반으로 하기 때문에 비슷한 문제이지 않을까 추측 중
- 해결 방법
    1. `sudo ln -s $HOME/.docker/run/docker.sock /var/run/docker.sock`([출처](https://stackoverflow.com/questions/61108655/test-container-test-cases-are-failing-due-to-could-not-find-a-valid-docker-envi))
       -> 해결 안 됨(`$HOME/.docker/run/docker.sock`가 local PC에 없기 때문)
    2. `intelliJ IDE > Preferences > Build, Execution, Deployment > Docker > TCP Socket > Engine API URL`
       변경([출처](https://stackoverflow.com/questions/74173489/docker-socket-is-not-found-while-using-intellij-idea-and-docker-desktop-on-macos))
       -> 해결 안 됨(왜 안되는지 이유 모르겠음)
        - 터미널 내 `$ docker context ls` 명령어 통해 나온 DOCKER_ENDPOINT 입력
    3. Rancher Desktop 의 도커 소켓 파일을 심볼릭
       걸기`([출처](https://stackoverflow.com/questions/74173489/docker-socket-is-not-found-while-using-intellij-idea-and-docker-desktop-on-macos))
       -> **해결 됨**
        - `sudo ln -svf /Users/nhn/.rd/docker.sock /var/run/docker.sock
        - 터미널 내 `$ docker context ls` 명령어 통해 나온 DOCKER_ENDPOINT를 /var/run/docker.sock에 링크로 연결