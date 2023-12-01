package com.example.unimeeting.service;

import com.example.unimeeting.domain.Meeting;
import com.example.unimeeting.dto.MainDTO;
import com.example.unimeeting.dto.MeetingWithDetailsDTO;
import com.example.unimeeting.repository.MeetingImageRepository;
import com.example.unimeeting.repository.MeetingRepository;
import com.example.unimeeting.repository.MemberRepository;
import com.example.unimeeting.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class MainService {
    private final MeetingRepository meetingRepository;
    private final MeetingImageRepository imageR;
    private final MemberRepository memberR;
    private final ScrapRepository scrapRepository;
    private static List<Meeting> list;
    public List<Meeting> findAll() { return list; }

    public List<Meeting> meetingsearch(String keyword){

        return meetingRepository.searchByList(keyword);
    }
//    public List<MeetingWithDetailsDTO> getMeetings() {
//
//    }

//    public List<Meeting> meetingpopular(){
//
//        return meetingRepository.findAllByOrderByScrab();
//    }

    public List<MainDTO> meetingtitle(){
        List<Meeting> list = meetingRepository.findAllByOrderByTitle();
        List<MainDTO> listDTO = new ArrayList<>();
        MainDTO dto;

        for(int i=0; i<list.size(); i++){
            Meeting m = list.get(i);
            List<String> imgUrls = imageR.findImageUrlByMeetingIdx(m.getIdx());
            String imgUrl = imgUrls.isEmpty() ? "":imgUrls.get(0);
            dto = new MainDTO(m, memberR.countByMeetingIdx(m.getIdx()) ,imgUrl);
            listDTO.add(dto);
        }
        return listDTO;

    }

    public List<MainDTO> meetingdatetime(){
        List<Meeting> list = meetingRepository.findAllByOrderByCreatedDatetimeDesc();
        List<MainDTO> listDTO = new ArrayList<>();
        MainDTO dto;

        for(int i=0; i<list.size(); i++){
            Meeting m = list.get(i);
            List<String> imgUrls = imageR.findImageUrlByMeetingIdx(m.getIdx());
            String imgUrl = imgUrls.isEmpty() ? "":imgUrls.get(0);
            dto = new MainDTO(m, memberR.countByMeetingIdx(m.getIdx()) ,imgUrl);
            listDTO.add(dto);
        }
        return listDTO;

    }

}
