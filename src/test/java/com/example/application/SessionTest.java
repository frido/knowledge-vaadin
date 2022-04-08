package com.example.application;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestApplication.class})
@ComponentScan(value = "com.example.application")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessionTest {

    @Autowired
	private TestRestTemplate restTemplate;

    @Test
	public void greetingShouldReturnDefaultMessage() throws Exception { 
        String response = this.restTemplate.getForObject("/rest-test", String.class); // TODO: volanie na MyRestController
        // TODO: chcem testovas session. 
        // TODO: pridat asserty do MyRestController, aby som overil session
        // Prezistit kde je ulozena session, kolko ich je a podobne
        // Nacitat bean zo session a porovnat realnou beanou
        // Ci su to rovnake instancie
        assertEquals("Hello, World", response);
	}
    
}
