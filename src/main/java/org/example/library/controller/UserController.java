package org.example.library.controller;

import org.example.library.model.Order;
import org.example.library.model.User;
import org.example.library.repository.OrderRepository;
import org.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/getAll")
    @ResponseBody
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/"; // Redirect to index page if user is already logged in
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String login, @RequestParam String password, HttpSession session, Model model) {
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            hashedPassword = no.toString(16);
            while (hashedPassword.length() < 32) {
                hashedPassword = "0" + hashedPassword;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        User user = userRepository.findByLoginAndPassword(login, hashedPassword);
        if (user != null) {
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("user", user);
            return "redirect:/"; // Redirect to main page
        } else {
            model.addAttribute("loginError", "Invalid login or password");
            return "login";
        }
    }

    @ModelAttribute
    public void addUserToModel(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

    @PostMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/login"; // Redirect to login page
    }

    @GetMapping("/profile")
    public String showProfilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // Redirect to login page if user is not logged in
        }
        List<Order> orders = orderRepository.findAllByid_user(user.getId_user());
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        return "profile";
    }
}