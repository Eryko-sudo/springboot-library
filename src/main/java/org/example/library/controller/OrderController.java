package org.example.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.library.model.Book;
import org.example.library.model.Order;
import org.example.library.model.User;
import org.example.library.repository.BookRepository;
import org.example.library.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BookRepository bookRepository;
    @GetMapping("/getAll")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam Integer bookId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Book book = bookRepository.findById(bookId).orElse(null);
            if (book != null && book.getPiece_count() > 0) {
                Order order = new Order();
                order.setId_user(user.getId_user());
                order.setId_book(bookId);
                order.setOrder_date(new Date());
                order.setReturn_by_date(Date.from(LocalDate.now().plusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                orderRepository.save(order);

                book.setPiece_count(book.getPiece_count() - 1);
                bookRepository.save(book);

                return "redirect:/order/success";
            }
        }
        return "redirect:/order/failure";
    }

}
