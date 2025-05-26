package kr.ac.hansung.cse.productcrud1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String redirectToProducts() {
        return "redirect:/products";
    }
}
