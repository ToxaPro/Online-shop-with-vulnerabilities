package com.example.diplom.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class SinkController {
    @GetMapping("")
    public String getIndex() {
        return "redirect:/products";
    }
}
