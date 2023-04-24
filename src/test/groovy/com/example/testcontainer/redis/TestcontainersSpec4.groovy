package com.example.testcontainer.redis

import com.example.testcontainer.redis.config.RedisTestcontainersSpec
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TestcontainersSpec4 extends RedisTestcontainersSpec {

    def "Redis 실행 여부 및 정보 확인"() {
        given:
        def host = "localhost"

        when:
        def port = redis.getMappedPort(REDIS_PORT)

        then:
        host == redis.host
        port == redis.getMappedPort(REDIS_PORT)
        redis.running
    }

    def "Redis 실행 여부 및 정보 확인 - 동시에 여러 테스트 실행시 Port 확인"() {
        given:
        def host = "localhost"

        when:
        def port = redis.getMappedPort(REDIS_PORT)

        then:
        host == redis.host
        port == redis.getMappedPort(REDIS_PORT)
        redis.running
    }
}
