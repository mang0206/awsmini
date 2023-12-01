package com.example.unimeeting.service;

import com.example.unimeeting.domain.Board;
import com.example.unimeeting.repository.BoardRepository;
import com.example.unimeeting.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    public Board save(Board board, Integer user_idx){
        board.setUser(userRepository.findById(user_idx).get());
        board.setCreatedDatetime(LocalDateTime.now());
        System.out.println(board);
        return boardRepository.save(board);
    }

    public List<Board> findByType(String type, String search) {
        return boardRepository.selectList(type,search);
    }

    public Board findById(long id) {
        return boardRepository.findById((int) id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }
    public void deleteById(long id){
        boardRepository.deleteById((int) id);
    }
    @Transactional
    public void update(long id, Board request) {
        Board board = boardRepository.findById((int) id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        board.update(request.getTitle(), request.getContent());
    }
}
