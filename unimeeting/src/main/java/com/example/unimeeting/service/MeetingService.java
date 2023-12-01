package com.example.unimeeting.service;


import com.example.unimeeting.domain.*;
import com.example.unimeeting.dto.*;
import com.example.unimeeting.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingImageRepository  meetingImageRepository;
    private final MemberRepository memberRepository;
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    public List<String> findCatogory(){
        return meetingRepository.findDistinctCategoryBy();
    }

    public Meeting findById(Integer id){
        return meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

//    public MeetingResponse getMeetingOne(Integer id, Integer user_idx){
//        User user = userRepository.findById(user_idx)
//                .orElseThrow(() -> new IllegalArgumentException);

    public MeetingResponse getMeetingOne(Integer id, Integer user_idx){
        User user = userRepository.findById(user_idx).
                orElse(null);
        return new MeetingResponse(meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id)),
                memberRepository.countByMeetingIdx(id),
                getMeetingImages(id),
                user != null && meetingRepository.existsByIdxAndUserNickname(id, user.getNickname()),
                user != null && memberRepository.existsByMeetingIdxAndUserIdx(id, user.getIdx()),
                user != null && scrapRepository.existsByMeetingIdxAndUserIdx(id, user.getIdx()));
    }

    public List<MeetingWithDetailsDTO> getAllMeeting(String search){
        List<MeetingWithDetailsDTO> list = new ArrayList<>();
        meetingRepository.findAllByTitleContainingOrContentContaining(search,search)
                .forEach(element -> list.add(new MeetingWithDetailsDTO(element,
                        memberRepository.countByMeetingIdx(element.getIdx()),
                        getMeetingImages(element.getIdx()).isEmpty() ?
                                "" :getMeetingImages(element.getIdx()).get(0))
                ));

//        List<MeetingWithDetailsDTO> list = (List<MeetingWithDetailsDTO>) meetingrepository.findAllByTitleContainingOrContentContaining(search,search)
//                .stream().map(e ->
//                        new MeetingWithDetailsDTO(e,
//                                memberRepository.countByMeetingIdx(e.getIdx()),
//                                meetingImageRepository.findImageUrlByMeetingIdx(e.getIdx()).get(0)));

        return list;
    }

    public List<MeetingWithDetailsDTO> getMeetingByCtgr(String category, String search){
        List<MeetingWithDetailsDTO> list = new ArrayList<>();
        meetingRepository.searchMeetingInCategory(category, search,search)
                .forEach(element -> list.add(new MeetingWithDetailsDTO(element,
                        memberRepository.countByMeetingIdx(element.getIdx()),
                        getMeetingImages(element.getIdx()).isEmpty() ?
                                "" :getMeetingImages(element.getIdx()).get(0))
                ));
        return list;
    }

    public Boolean addMeeting(AddMeetingRequest request, int user_idx, MultipartFile[] mreq){
        try{
            User user = userRepository.findById(user_idx).get();
            Meeting meeting = meetingRepository.save(request.toEntity(user));
            int meeting_idx = meeting.getIdx();

            if(mreq != null){
            List<MultipartFile> list = Arrays.stream(mreq).toList();

                String path = "/images/" + meeting_idx;
                // 상대 경로를 찾는 함수인 getRealPath()는 프로젝트 폴더 구조에서 resources가 아닌 webapp 폴더를 우선으로 찾고
                //  해당 폴더가 존재하지 않으면 위와 같이 임시 폴더를 찾아간다.
                // webapp 폴더를 만드는 방법도 있으나, Spring Boot는 jar로 배포되기 때문에 webapp 폴더를 만든다면 정상 배포 되지 않는다.
//                String realPath = "C:/kosastudy/unimeeting/unimeeting/src/main/resources/static" + path;
                String realPath = "C:/workspace/UniMeeting2/unimeeting/src/main/resources/static" + path;
                File isDir = new File(realPath);
                if (!isDir.isDirectory()) {
                    isDir.mkdirs();
                }

                for (MultipartFile mfile : list) {
                    // replace -> 파일 이름의 공백을 언더바로 변경
                    String fileName = mfile.getOriginalFilename().replace(" ", "_");
                    System.out.println(fileName);

                    try {
                        File f = new File(realPath + "/"+ fileName);
                        if (f.exists()) {
                            System.out.println("already exist");
                        } else {
                            mfile.transferTo(f);
                            MeetingImage meetingImage = MeetingImage.builder()
                                    .meetingIdx(meeting_idx)
                                    .imageUrl(path+"/"+fileName)
                                    .build();
                            meetingImageRepository.save(meetingImage);
                            System.out.println("upload images success");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("upload images error");
                    }
                }
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMeeting(Integer id, UpdateMeetingRequest update,MultipartFile[] mreq){
        boolean isSuccess = false;
        Optional<Meeting> meeting =meetingRepository.findById(id);
        Meeting oldMeeting;
        if(meeting.isPresent()){
            oldMeeting = meeting.get();
            oldMeeting.update(update.getTitle(),
                    update.getContent(), update.getCategory(),
                    update.getRecruits(), update.getLocation(), update.getStartDatetime());
            int meeting_idx = oldMeeting.getIdx();

            if(mreq != null){
                List<MultipartFile> list = Arrays.stream(mreq).toList();

                String path = "/images/" + meeting_idx;
                // 상대 경로를 찾는 함수인 getRealPath()는 프로젝트 폴더 구조에서 resources가 아닌 webapp 폴더를 우선으로 찾고
                //  해당 폴더가 존재하지 않으면 위와 같이 임시 폴더를 찾아간다.
                // webapp 폴더를 만드는 방법도 있으나, Spring Boot는 jar로 배포되기 때문에 webapp 폴더를 만든다면 정상 배포 되지 않는다.
//                String realPath = "C:/kosastudy/unimeeting/unimeeting/src/main/resources/static" + path;
                String realPath = "C:/workspace/UniMeeting2/unimeeting/src/main/resources/static" + path;
                File isDir = new File(realPath);
                if (!isDir.isDirectory()) {
                    isDir.mkdirs();
                }

                for (MultipartFile mfile : list) {
                    // replace -> 파일 이름의 공백을 언더바로 변경
                    String fileName = mfile.getOriginalFilename().replace(" ", "_");
                    System.out.println(fileName);

                    try {
                        File f = new File(realPath + "/"+ fileName);
                        if (f.exists()) {
                            System.out.println("already exist");
                        } else {
                            mfile.transferTo(f);
                            MeetingImage meetingImage = MeetingImage.builder()
                                    .meetingIdx(meeting_idx)
                                    .imageUrl(path+"/"+fileName)
                                    .build();
                            meetingImageRepository.save(meetingImage);
                            System.out.println("upload images success");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("upload images error");
                    }
                }
            }
            isSuccess = true;
        }
        return isSuccess;
    }

    public boolean deleteMeeting(Integer id){
        boolean isSuccess = false;
        if (meetingRepository.findById(id).isPresent()) {
            meetingRepository.deleteById(id);
            isSuccess = true;
        }
        return isSuccess;
    }

    public Member addMember(Integer meeting_idx,Integer user_idx){
        User user = userRepository.findById(user_idx).get();
        AddMemberRequest addMemberRequest = new AddMemberRequest();
        addMemberRequest.setMeetingIdx(meeting_idx);
        addMemberRequest.setUser(user);
        return memberRepository.save(addMemberRequest.toEntity());
    }

    public boolean updateMember(Integer id){

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        boolean isSuccess = false;

        if (memberRepository.findById(id).isPresent()) {
            member.update();
            isSuccess = true;
        }
        return isSuccess;
    }

    public boolean deleteMember(Integer meeting_idx, Integer user_idx){
        boolean isSuccess = false;
        Member member = memberRepository.findByMeetingIdxAndUserIdx(meeting_idx, user_idx);
        if (member!=null) {
            memberRepository.deleteById(member.getIdx());
            isSuccess = true;
        }
        return isSuccess;
    }

    public List<MemberResponse> getMember(Integer id){
        List<MemberResponse> list = new ArrayList<>();
        memberRepository.findByMeetingIdx(id)
                .forEach(member ->  list.add(new MemberResponse(member)));
        return list;
    }

    public boolean addScrap(Integer meeting_id,Integer user_idx){
        boolean success = false;
        try{
            User user = userRepository.findById(user_idx)
                    .orElseThrow(()-> new IllegalArgumentException());
            Scrap scrap = new Scrap(user, meeting_id);
            success = true;
            scrapRepository.save(scrap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;
    }

    public boolean deleteScrap(Integer meeting_id, Integer user_idx){
        boolean success = false;
        try{
            Scrap scrap = scrapRepository.findByMeetingIdxAndUserIdx(meeting_id, user_idx);
            if(scrap!=null){
                success= true;
                scrapRepository.deleteById(scrap.getIdx());
            }else{
                success = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return  success;
    }

    public List<String> getMeetingImages(int meeting_idx){
        List<String> list = meetingImageRepository.findImageUrlByMeetingIdx(meeting_idx);
        return list;
    }

    public boolean deleteImage(int meeting_idx, String url){
        boolean isSuc = false;

        try{
            MeetingImage image = meetingImageRepository.findByMeetingIdxAndImageUrl(meeting_idx, url);
            meetingImageRepository.delete(image);
            isSuc = true;
        }catch (Exception e ){
            e.printStackTrace();
        }

        return  isSuc;
    }


}
