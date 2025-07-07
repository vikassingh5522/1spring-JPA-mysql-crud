package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }

    @GetMapping("/user/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(user.getId(), user.getName());
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/add";
        }
    }

    @GetMapping("/user/edit/{id}")
    public String showEditForm(@PathVariable("id") String userId, Model model) {
        try {
            User user = userService.getUserById(userId);
            model.addAttribute("user", user != null ? user : new User());
            return "user-form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("user", new User());
            return "user-form";
        }
    }

    @PostMapping("/user/edit/{id}")
    public String updateUser(@PathVariable("id") String userId, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(userId, user.getName());
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/edit/" + userId;
        }
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") String userId, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(userId);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("errorMessage", "An error occurred: " + e.getMessage());
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }
}