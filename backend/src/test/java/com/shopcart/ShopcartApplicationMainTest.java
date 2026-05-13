package com.shopcart;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class ShopcartApplicationMainTest {

    @Test
    void testMainInvokesSpringApplicationRun() {
        String[] args = new String[] { "--spring.profiles.active=test" };

        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            ShopcartApplication.main(args);
            springApplicationMock.verify(() -> SpringApplication.run(ShopcartApplication.class, args));
        }
    }
}
