package com.healthsystem.controller;

import com.healthsystem.model.User;
import com.healthsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user-center")
    public String userCenter(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // 未登录，重定向到登录页面
        }
        User user = userService.getUserById(userId); // 从服务层加载用户
        if (user == null) {
            return "redirect:/login"; // 用户不存在，重定向
        }
        model.addAttribute("user", user); // 传递 User 对象到视图
        return "pages/user-center";
    }

    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute User user, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        user.setId(userId); // 设置用户 ID
        userService.updateUser(user); // 更新用户数据
        session.setAttribute("userId", userId); // 刷新会话（可选）
        return "redirect:/user-center";
    }
}