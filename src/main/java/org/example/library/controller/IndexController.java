package org.example.library.controller;

import org.example.library.model.Book;
import org.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Collections;

@Controller
public class IndexController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Book> books = bookRepository.findAll();
        Collections.shuffle(books);
        model.addAttribute("books", books.subList(0, Math.min(books.size(), 3)));
        return "index";
    }

    @PostMapping("/search")
    public String search(@RequestParam("search_term") String query, Model model) {
        List<Book> books = bookRepository.search(query);
        model.addAttribute("books", books);
        return "search";
    }
}