package com.example.diplom.util;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;

public class RedirectionUtils {
    public static String redirectToErrors(Model model, String errorMessage, HttpServletResponse response) {
        model.addAttribute("message", errorMessage);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "error";
    }
}
