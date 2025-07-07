package com.example.controller;

import com.example.service.PaginatedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaginatedUserController {

    @Autowired
    private PaginatedUserService paginatedUserService;

    @GetMapping("/paginated-user-list")
    public String paginatedList(Model model,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("users", paginatedUserService.getPaginatedUsers(page, size));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalUsers", paginatedUserService.getTotalUsers());
        return "paginated-user-list";
    }

    @GetMapping("/sorted-user-list")
    public String sortedList(Model model,
                             @RequestParam(defaultValue = "id") String sortBy,
                             @RequestParam(defaultValue = "asc") String sortDir) {
        model.addAttribute("users", paginatedUserService.getSortedUsers(sortBy, sortDir));
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        return "sorted-user-list";
    }
}
