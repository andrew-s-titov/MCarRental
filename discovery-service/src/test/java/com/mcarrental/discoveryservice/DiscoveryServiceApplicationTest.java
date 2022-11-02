package com.mcarrental.discoveryservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiscoveryServiceApplicationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void catalogLoads() {
        ResponseEntity<Object> response = testRestTemplate.getForEntity("/eureka/apps", Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
