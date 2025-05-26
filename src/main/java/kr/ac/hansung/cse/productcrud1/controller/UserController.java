package kr.ac.hansung.cse.productcrud1.controller;

import kr.ac.hansung.cse.productcrud1.entity.User;
import kr.ac.hansung.cse.productcrud1.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password) {
        userService.registerUser(email, password, User.Role.ROLE_USER);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
}
