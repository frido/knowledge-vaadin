package com.example.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyRestController {

	@RequestMapping("/rest-test")
	public @ResponseBody String greeting() {
		return "Hello, World";
	}

}
