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

/**
 * Application server (Tomcat) creates HttpSession (StandardSession) in request when somebody call
 * request.getSession() This HttpSession is store for session related objects. It is stored in
 * roperty Attributes. There can anybody store anything to be keepted in sesson scope.
 * 
 * Filters operate with session to get/set values from HttpSession.attributes.
 * 
 * Spring SecurityContextFilter (cca) get from HttpSession.attribute(this.springSecurityContextKey)
 * SecurityContext and store it in LocalThread variable SecurityContextHolder.getContext(). So we
 * can call SecurityContextHolder.getContext() to get security context for the application. After
 * request the value store in security context is stored back to the HttpSession.attributes.
 * 
 * Vaadin application servlet/filter creates VaadinSession (it has nothing to do with security) that
 * is stored also in HttpSession.attributes. There are values related for the vaadin application.
 * (client, version...)
 * 
 * Spring stores session beans to the HttpSession.attributes.
 * 
 * All of this is stored in the HttpSession that is handled by Tomcat. When session expires,
 * everything (VaadinSession, SecurityContext) is lost.
 * 
 * SecurityContext is most importatnt for security of application. There is stored info about user
 * (if no user, spring call it anonymous). SecurityContext keeps Authentication object. One of these
 * implementation is UsernamePasswordAuthenticationToken. Security rules defined in
 * WebSecurityConfigurerAdapter.configure(HttpSecurity http). For example
 * .anyRequest().authenticated() says Authenticated object (from Spring Context) has to have
 * authenticated flag = true. We define bean AuthenticationManager that authenticated user as just
 * set authenticated = true; We can also defina roles and permit acces based on roles (not tested).
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestApplication.class})
@ComponentScan(value = "com.example.application")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessionTest {

    @LocalServerPort
    private int port;

    @Autowired
    MyRestController controller;

    @Autowired
    AuthenticationManager authenticationManager;


    /**
     * Call login rest to authenticate user and then call rest to increment value in session bean.
     * 
     * @throws Exception
     */
    @Test
    public void createSession() throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCookieStore(new MyCookieStore()).disableRedirectHandling().build();

        /**
         * /rest-test-login set Authentication object to the SecurityContext. Then it is stored
         * automaticaly by spring to the HttpSession. This test class has also CookieStore so cookie
         * JSESSIONID is stored in the cookies list. We keep cookies to use it in next requests so
         * HttpSession in Tomcat is restored.
         * 
         * Service increments and return int.
         */
        HttpResponse response = httpClient.execute(createRequest("/rest-test-login"));
        response.getEntity().getContent().close();

        // TODO: https://www.baeldung.com/awaitility-testing
        Thread.sleep(1000); // requests can be paralel so wait

        /**
         * /rest-test increments and return int. It is restricted path so user have to be
         * authenticated. And he is becouse we reuse JSESSIONID. HttpSession is restored and in the
         * attributes it contains SecurityContext where is stored Authentication that we created in
         * the rest-test-login call.
         */
        HttpResponse response2 = httpClient.execute(createRequest("/rest-test"));
        String responseBody2 = EntityUtils.toString(response2.getEntity());
        assertEquals("2", responseBody2);
        response2.getEntity().getContent().close();

        Thread.sleep(1000);

        /**
         * /rest-test increments and return int. Test that value is session is incremented and the
         * session bean is not the new one.
         */
        HttpResponse response3 = httpClient.execute(createRequest("/rest-test"));
        String responseBody3 = EntityUtils.toString(response3.getEntity());
        assertEquals("3", responseBody3);
        response3.getEntity().getContent().close();

        Thread.sleep(1000);
    }

    private HttpGet createRequest(String link) {
        return new HttpGet("http://localhost:" + port + link);
    }

    /**
     * Call restricted rest without authentication. It should return redirect status code because
     * and().formLogin().loginPage so without authentication user is redirected to the login page.
     * 
     * @throws Exception
     */
    @Test
    public void createSession2() throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCookieStore(new MyCookieStore()).disableRedirectHandling().build();

        HttpGet getRequest = createRequest("/rest-test");
        HttpResponse response = httpClient.execute(getRequest);
        assertEquals(302, response.getStatusLine().getStatusCode()); // redirect to login
    }

    private class MyCookieStore implements CookieStore {

        private List<Cookie> cookies = new ArrayList<>();

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
}
