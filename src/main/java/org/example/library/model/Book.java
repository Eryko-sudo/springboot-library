package org.example.library.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_book;
    private Integer id_category;
    private String isbn;
    private String title;
    private String author;
    private String page_count;
    private String publisher;
    private String release_date;
    private String description;
    private String url;
    private Integer piece_count;
}