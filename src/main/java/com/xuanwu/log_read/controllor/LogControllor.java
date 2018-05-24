package com.xuanwu.log_read.controllor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogControllor {
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}

}
