package com.example.unimeeting;

import com.example.unimeeting.controller.MainController;
import com.example.unimeeting.domain.Meeting;
import com.example.unimeeting.dto.MainDTO;
import com.example.unimeeting.repository.MeetingImageRepository;
import com.example.unimeeting.repository.MeetingRepository;
import com.example.unimeeting.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.example.unimeeting.repository.MeetingRepository;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
public class MainControllertest {

    @Autowired
    private MeetingRepository repository;
    @Autowired
    MeetingImageRepository imageR;
    @Autowired
    MemberRepository memberR;
    @BeforeEach()
    void pr() {
        System.out.println("=".repeat(80));
    }


    @Test
    @Order(1)
    public void findAll() {
        List<Meeting> list = repository.findAll();
        list.forEach(System.out::println);
    }

    @Test
    @Order(2)
    public void searchByList(){
        List<Meeting> list = repository.searchByList("");
        list.forEach(System.out::println);
    }

//    @Test
//    @Order(3)
//    public void findAllByOrderByScrab() {
//        List<Meeting> list = repository.findAllByOrderByScrab();
//        list.forEach(System.out::println);
//    }

    @Test
    @Order(4)
    public void findAllByOrderByTitle(){
        List<Meeting> list = repository.findAllByOrderByTitle();
        list.forEach(System.out::println);
    }
    @Test
    @Order(5)
    public void findAllByOrderByCreatedDatetime() {
        List<Meeting> list = repository.findAllByOrderByCreatedDatetimeDesc();
        list.forEach(System.out::println);
    }

    //@Test
    @Order(6)
    void testt1(){
        List<Meeting> list = repository.findAllByOrderByTitle();
        List<MainDTO> listDTO = new ArrayList<>();
        MainDTO dto;

        for(int i=0; i<list.size(); i++){
            Meeting m = list.get(i);
            List<String> imgUrls = imageR.findImageUrlByMeetingIdx(m.getIdx());
            String imgUrl = imgUrls.isEmpty() ? "":imgUrls.get(0);
            dto = new MainDTO(m, memberR.countByMeetingIdx(m.getIdx()) ,imgUrl);
            listDTO.add(dto);
        }
        listDTO.stream().forEach(l -> {
            System.out.println(l.getIdx()+"//" +l.getCreatedDatetime() + l.getImageUrl());
        });
    }


    //@Test
//    @Order(7)
//    public void searchMeetingByOrderByCreatedDatetime() {
//        List<Meeting> list = repository.searchMeetingByOrderByCreatedDatetime("환영합니다");
//        list.forEach(System.out::println);
//    }
}
