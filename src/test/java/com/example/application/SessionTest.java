package com.example.application;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.application.views.security.MySessionBean;
import com.vaadin.flow.server.VaadinSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestApplication.class})
@ComponentScan(value = "com.example.application")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessionTest implements CookieStore {

    @LocalServerPort
    private int port;

    @Autowired
    MyRestController controller;

    @Autowired
    AuthenticationManager authenticationManager;

    List<Cookie> cookies = new ArrayList<>();

    /**
     * SecurityContextHolder.getContext() -> httpSession.getAttribute(this.springSecurityContextKey)
     * 
     * @throws Exception
     */
    @Test
    public void createSession() throws Exception {
        CloseableHttpClient httpClient =
                HttpClientBuilder.create().setDefaultCookieStore(this).disableRedirectHandling().build();

        // create request to login
        HttpGet getRequest = new HttpGet("http://localhost:" + port + "/rest-test-login");
        HttpResponse response = httpClient.execute(getRequest);
        response.getEntity().getContent().close();

        Thread.sleep(1000);

        // call as authenticated user
        HttpGet getRequest2 = new HttpGet("http://localhost:" + port + "/rest-test");
        HttpResponse response2 = httpClient.execute(getRequest2);
        String r2 = EntityUtils.toString(response2.getEntity());
        assertEquals("2", r2);
        response2.getEntity().getContent().close();

        Thread.sleep(1000);

        HttpGet getRequest3 = new HttpGet("http://localhost:" + port + "/rest-test");
        HttpResponse response3 = httpClient.execute(getRequest3);
        String r3 = EntityUtils.toString(response3.getEntity());
        assertEquals("3", r3);
        response3.getEntity().getContent().close();

        Thread.sleep(1000);

        assertEquals(1, cookies.size());;
    }

    @Test
    public void createSession2() throws Exception {
        CloseableHttpClient httpClient =
                HttpClientBuilder.create().setDefaultCookieStore(this).disableRedirectHandling().build();

        // create request to login
        HttpGet getRequest = new HttpGet("http://localhost:" + port + "/rest-test");
        HttpResponse response = httpClient.execute(getRequest);
        assertEquals(302, response.getStatusLine().getStatusCode()); // redirect to login

        Thread.sleep(1000);

        assertEquals(1, cookies.size());;
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public List<Cookie> getCookies() {
        return cookies;
    }

    @Override
    public boolean clearExpired(Date date) {
        return false;
    }

    @Override
    public void clear() {

    }

}
