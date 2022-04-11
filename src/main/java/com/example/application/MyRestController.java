package com.example.application;

import javax.servlet.http.HttpSession;
import com.example.application.views.security.MySessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyRestController {

    public static final String USER = "user";

    @Autowired 
    MySessionBean sessionBean;

    @Autowired 
    HttpSession session;

    @RequestMapping("/rest-test-session")
	public @ResponseBody String restTestSession() {
        sessionBean.increment();
		return String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}

    @RequestMapping("/rest-test-increment")
	public @ResponseBody String restTestIncrement() {
        sessionBean.increment();
		return String.valueOf(sessionBean.getCounter());
	}

    @RequestMapping("/rest-test-login")
	public @ResponseBody String restTestLogin() {
        sessionBean.increment();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(USER, "pwd"));
        return session.getId();
	}
}
