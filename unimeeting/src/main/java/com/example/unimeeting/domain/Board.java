package com.example.unimeeting.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name="board")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private String title;
    private String content;
    private String type;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH-mm")
    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_nickname", referencedColumnName = "nickname")
    private User user;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
