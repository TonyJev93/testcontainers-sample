package com.example.testcontainer.redis;

import com.example.testcontainer.redis.config.RedisTestcontainersConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(RedisTestcontainersConfig.class)
@SpringBootTest
public class TestcontainersTest2 {

    @Test
    @DisplayName("RedisTestcontainersConfig 정상 여부 확인1")
    void test1() {
        var str = "hello";

        assertThat(str).isNotNull()
                .startsWith("he")
                .endsWith("o")
                .hasSize(str.length());
    }

    @Test
    @DisplayName("RedisTestcontainersConfig 정상 여부 확인2")
    void test2() {
        var str = "hello";

        assertThat(str).isNotNull()
                .startsWith("he")
                .endsWith("o")
                .hasSize(str.length());
    }
}
