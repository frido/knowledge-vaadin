package com.example.application;

import javax.servlet.http.HttpSession;
import com.example.application.views.security.MySessionBean;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.function.ServerResponse.Context;

@Controller
public class MyRestController {

	private VaadinSession vaadinSession;
    private SecurityContext springSession;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired 
    MySessionBean sessionBean;

    @RequestMapping("/rest-test")
	public @ResponseBody String greeting(@Autowired HttpSession session) {
        vaadinSession = VaadinSession.getCurrent();
        springSession = SecurityContextHolder.getContext();
        sessionBean.call();
		return String.valueOf(sessionBean.getX());
	}

    @RequestMapping("/rest-test-login")
	public @ResponseBody String greeting2(@Autowired HttpSession session) {

        final Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken("user", "pwd"));
        if(authentication != null ) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        sessionBean.call();
        return session.getId();
	}

    public VaadinSession getVaadinSession() {
        return vaadinSession;
    }

    public SecurityContext getSpringSession() {
        return springSession;
    }

    public MySessionBean getSessionBean() {
        return sessionBean;
    }

}
