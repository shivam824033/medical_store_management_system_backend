package com.medical.store.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class WebController {

	  @RequestMapping(value = {"/{path:[^\\.]*}", "/**/{path:[^\\.]*}"})
	    public String redirect(HttpServletRequest request) {
	        String uri = request.getRequestURI();

	        // Allow API routes to pass through
	        if (uri.startsWith("/api")) {
	            return null;
	        }

	        // ✅ IMPORTANT FIX:
	        // Do not forward index.html requests; let Spring serve it directly
	        if (uri.equals("/index.html") || uri.equals("/")) {
	            return "forward:/index.html";
	        }

	        // Forward all other routes to Angular's index.html
	        return "forward:/index.html";
	    }
}
