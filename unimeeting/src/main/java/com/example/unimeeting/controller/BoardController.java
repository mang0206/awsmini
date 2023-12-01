package com.example.unimeeting.controller;

import com.example.unimeeting.domain.Board;
import com.example.unimeeting.domain.User;
import com.example.unimeeting.repository.UserRepository;
import com.example.unimeeting.service.BoardService;
import com.example.unimeeting.service.JwtService;
import com.example.unimeeting.service.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    User user = new User(52, "aelim", "1234", "aa", "devaelim@gmail.com", "코딩", "01092708011", "USER");

    private final BoardService boardService;
    private final JwtServiceImpl jwtService;
    //=============글 목록 ===========//
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Board>> getAllBoards(@PathVariable String type, @RequestParam(required = false) String search) {
        List<Board> board = boardService.findByType(type,search);
        return ResponseEntity.ok(board);
    }


    //=============글 상세 ===========//
    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable int id) {
        Board board = boardService.findById(id);
        return ResponseEntity.ok(board);
    }
    //=============글 쓰기 ===========//
    @PostMapping("/write")
    public ResponseEntity<String> createBoard(@RequestBody Board board,@RequestHeader(required = false, value = "Authorization") String token) {

        boardService.save(board, jwtService.getId(token));

        return ResponseEntity.status(HttpStatus.CREATED).body("Board created successfully");
    }


    //=============글 수정 ===========//

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBoard(@PathVariable int id, @RequestBody Board board) {
        boardService.update(id, board);
        return ResponseEntity.ok("Board updated successfully");
    }
    //=============글 삭제 ==============//
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable int id) {
        boardService.deleteById(id);
        return ResponseEntity.ok("Board deleted successfully");
    }

}
