package com.example.testcontainer.redis.config

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class RedisTestcontainersSpec extends Specification {
    public static final REDIS_PORT = 6379
    public static final REDIS_IMAGE = "redis:7.0.8-alpine"

    // Class 당 한 번 수행
    @Shared
    GenericContainer redis = new GenericContainer(DockerImageName.parse(REDIS_IMAGE))
            .withExposedPorts(REDIS_PORT)
            .waitingFor(Wait.forListeningPort())

    def "setupSpec"() {
        // Redis 시작
        redis.start() // redis.getMappedPort(REDIS_PORT) 보다 먼저 Redis가 실행 돼야 함.
        syncRedisPropertiesWithTestcontainers()

        def redisServer = redis.host + ":" + redis.getMappedPort(REDIS_PORT)
        println "redis started >> server = " + redisServer
    }

    private void syncRedisPropertiesWithTestcontainers() {
        System.setProperty("spring.data.redis.host", redis.host)

        // 실제 바인딩 된 PORT를 찾기 위함
        System.setProperty("spring.data.redis.port", String.valueOf(redis.getMappedPort(REDIS_PORT)))
    }

    def "cleanupSpec"() {
        // Redis 종료
        redis.close()
    }
}
