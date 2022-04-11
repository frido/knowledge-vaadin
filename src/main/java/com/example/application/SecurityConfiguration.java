package com.example.application;

import com.example.application.views.security.CustomRequestCache;
import com.example.application.views.security.SecurityLoginView;
import com.example.application.views.security.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Configures spring security, doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form</li>
 * 
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	/**
	 *
	 */
	public static final String REST_TEST_LOGIN = "rest-test-login";
	private static final String LOGOUT_SUCCESS_URL = "/";

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return new AuthenticationManager() {

			@Override
			public Authentication authenticate(Authentication authentication)
					throws AuthenticationException {
				return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
						authentication.getCredentials(), authentication.getAuthorities());
			}
		};
	}

	@Bean
	public CustomRequestCache requestCache() {
		return new CustomRequestCache();
	}

	/**
	 * Require login to access internal pages and configure login form.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Not using Spring CSRF here to be able to use plain HTML for the login page
		http.csrf().disable()

				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()

				// Register our CustomRequestCache, that saves unauthorized access attempts, so
				// the user is redirected after login.
				.requestCache().requestCache(requestCache())

				// .and().httpBasic()

				// Restrict access to our application.
				.and().authorizeRequests()

				// Allow all flow internal requests.
				.requestMatchers(request -> SecurityUtils.isFrameworkInternalRequest(request)
						|| request.getRequestURI().contains(REST_TEST_LOGIN))
				.permitAll()

				// Allow all requests by logged in users.
				.anyRequest().authenticated()

				// Configure the login page.
				.and().formLogin().loginPage("/" + SecurityLoginView.ROUTE).permitAll()

				// Configure logout
				.and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
	}

	/**
	 * Allows access to static resources, bypassing Spring security.
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				// "/rest-test/**",

				// Vaadin Flow static resources
				"/VAADIN/**",

				// the standard favicon URI
				"/favicon.ico",

				// the robots exclusion standard
				"/robots.txt",

				// web application manifest
				"/manifest.webmanifest", "/sw.js", "/offline-page.html",

				// icons and images
				"/icons/**", "/images/**",

				// (development mode) static resources
				"/frontend/**",

				// (development mode) webjars
				"/webjars/**",

				// (development mode) H2 debugging console
				"/h2-console/**",

				// (production mode) static resources
				"/frontend-es5/**", "/frontend-es6/**");
	}
}
