package com.example.unimeeting.controller;

import com.auth0.jwt.interfaces.Claim;
import com.example.unimeeting.config.jwt.JwtProperties;
import com.example.unimeeting.domain.Meeting;
import com.example.unimeeting.domain.Scrap;
import com.example.unimeeting.dto.MeetingResponse;
import com.example.unimeeting.service.JwtService;
import com.example.unimeeting.service.JwtServiceImpl;
import com.example.unimeeting.service.MeetingService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.unimeeting.domain.Member;
import com.example.unimeeting.domain.User;
import com.example.unimeeting.dto.*;
import com.example.unimeeting.service.MeetingService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meetings")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final JwtServiceImpl jwtService;

    @GetMapping("/category")
    public ResponseEntity<List<String>> getCategory(){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(meetingService.findCatogory());
    }

    // 미팅 글 리스트 조회
    @GetMapping
    public ResponseEntity<List<MeetingWithDetailsDTO>> getMeetings(@RequestParam(value ="ctgr", required = false) String category,
                                                                   @RequestParam(defaultValue = "") String search){
        List<MeetingWithDetailsDTO> response;
        System.out.println(category);
        // category 는 필수 요청이 아님, null 이라면 모든 글 조회. 전달된 값이 있다면 해당 category 글 조회
        // search 는 default "". 아무 것도 전달 받지 않으면 SQL 에서 (SELECT ~ LIKE "") 이므로 검색어 없이 조회 가능.
        response = category == null? meetingService.getAllMeeting(search) : meetingService.getMeetingByCtgr(category,search);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // 미팅 글 하나 조회
    @GetMapping("/{idx}")
    public ResponseEntity<MeetingResponse> getOneMeeting(@RequestHeader (value = JwtProperties.HEADER_STRING, required = false) String token, @PathVariable int idx){
        int user_idx = 0;
        if(token!=null){
            user_idx = jwtService.getId(token);
        }
        MeetingResponse response = meetingService.getMeetingOne(idx,user_idx);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    // 미팅 글 생성
    // JsonFormat, String 타입으로 전달되는 createdDateTime 을  LocalDateTime 타입으로 인식하기 위해 설정
    @PostMapping(consumes = "multipart/form-data")
//    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm")
    public ResponseEntity<CudResponse> uploadMeeting(@RequestHeader(value = JwtProperties.HEADER_STRING, required = false) String token, @RequestPart(value = "meetingData") @Valid AddMeetingRequest request, @RequestPart(value = "file", required = false) MultipartFile[] mreq) {

        CudResponse cudResponse = new CudResponse();
        int user_idx = jwtService.getId(token);
        boolean isSuc = meetingService.addMeeting(request, user_idx, mreq);
        ResponseEntity<CudResponse> response;
        cudResponse.setSuccess(isSuc);
        cudResponse.setMessage(isSuc ? "글이 작성되었습니다.": "작성 도중 오류가 발생했습니다.");

        return ResponseEntity
                .status(isSuc? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(cudResponse);
    }

    // 수정할 글 불러오기
    @GetMapping("/update/{idx}")
    public ResponseEntity<UpdateMeetingResponse> getMeetingForUpdate(@PathVariable int idx){
        UpdateMeetingResponse response = new UpdateMeetingResponse();
        response.setMeeting(meetingService.findById(idx));
        response.setImgUrl(meetingService.getMeetingImages(idx));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // 미팅 글 수정
    @PutMapping(value = "/{idx}",consumes = "multipart/form-data")
    @Transactional
    public ResponseEntity<CudResponse> updateMeeting( @PathVariable int idx, @RequestPart(value = "meetingData") @Valid  UpdateMeetingRequest update,@RequestPart(value = "file", required = false) MultipartFile[] mreq){
        HttpStatus status;
        CudResponse response = new CudResponse();
        if (meetingService.updateMeeting(idx, update, mreq)){
            status = HttpStatus.OK;
            response.setSuccess(true);
            response.setMessage("수정이 완료되었습니다.");
        }else{
            status = HttpStatus.BAD_REQUEST;
            response.setSuccess(false);
            response.setMessage("처리 도중 오류가 발생했습니다. \n다시 시도해 주세요.");
        }
        return ResponseEntity
                .status(status)
                .body(response);
    }

    // 미팅 글 삭제
    @DeleteMapping("/{idx}")
    public ResponseEntity<CudResponse> deleteMeeting(@PathVariable int idx){
        HttpStatus status;
        CudResponse response = new CudResponse();
        if (meetingService.deleteMeeting(idx)){
            status = HttpStatus.OK;
            response.setSuccess(true);
            response.setMessage("삭제되었습니다.");
        }else{
            status = HttpStatus.BAD_REQUEST;
            response.setSuccess(false);
            response.setMessage("처리 도중 오류가 발생했습니다. \n다시 시도해 주세요.");
        }
        return ResponseEntity
                .status(status)
                .body(response);
    }
    @PostMapping("/apply/{meeting_idx}")
    public ResponseEntity<CudResponse> addMember(@RequestHeader (value = JwtProperties.HEADER_STRING, required = false) String token, @PathVariable int meeting_idx){

        System.out.println("Controller token-------------------------"+token);
        CudResponse response = new CudResponse();
        if(token == null ){
            response.setSuccess(false);
            response.setMessage("로그인 후 이용 가능한 서비스입니다.");
        }else{
            System.out.println("참가신청 토큰");
            int user_idx = jwtService.getId(token);

            try{
                Member member = meetingService.addMember(meeting_idx, user_idx);
                response.setSuccess(true);
                response.setMessage("신청이 완료되었습니다!");
            }catch (Exception e){
                response.setSuccess(false);
                response.setMessage("처리 도중 오류가 발생했습니다. \n다시 시도해 주세요.");
                e.printStackTrace();
            }

        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/apply/{idx}")
    @Transactional
    public ResponseEntity<CudResponse> acceptAcceptApply(@PathVariable int idx){
        HttpStatus status;
        CudResponse response = new CudResponse();
        if (meetingService.updateMember(idx)){
            status = HttpStatus.OK;
            response.setSuccess(true);
            response.setMessage("완료되었습니다.");
        }else{
            status = HttpStatus.BAD_REQUEST;
            response.setSuccess(false);
            response.setMessage("처리 도중 오류 발생");
        }
        return ResponseEntity
                .status(status)
                .body(response);
    }

    // User = 신청자 or 글 작성자
    @DeleteMapping("/apply/{meeting_idx}")
        public ResponseEntity<CudResponse> deleteMember(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable int meeting_idx, @RequestParam(required = false) Integer user_idx){
        HttpStatus status;
        CudResponse response = new CudResponse();

        // 사용자가 신청을 취소한 경우, user_idx를 token에서 가져옴
        // 글 작성자가 신청을 거절한 경우, parameter로 오는 user_idx를 이용해 삭제
        if( user_idx == null ){
            user_idx = jwtService.getId(token);
        }

        if (meetingService.deleteMember(meeting_idx, user_idx)){
            status = HttpStatus.OK;
            response.setSuccess(true);
            response.setMessage("신청이 취소되었습니다.");
        }else{
            status = HttpStatus.BAD_REQUEST;
            response.setSuccess(false);
            response.setMessage("처리 도중 오류 발생");
        }
        return ResponseEntity
                .status(status)
                .body(response);
    }

    @GetMapping("/applicants/{idx}")
    public  ResponseEntity<List<MemberResponse>> getMember(@PathVariable int idx){
        List<MemberResponse> list = meetingService.getMember(idx);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }

    @PostMapping("/scrap/{meeting_idx}")
    public ResponseEntity<CudResponse> addScrap(@RequestHeader (value = "Authorization", required = false) String token, @PathVariable Integer meeting_idx){

        CudResponse response = new CudResponse();
        if(token == null ){
            response.setSuccess(false);
            response.setMessage("로그인 후 이용 가능한 서비스입니다.");
        }else {
            int user_idx = jwtService.getId(token);
            if (meetingService.addScrap(meeting_idx, user_idx)) {
                response.setSuccess(true);
                response.setMessage("스크랩이 완료되었습니다!");
            } else {
                response.setSuccess(false);
                response.setMessage("처리 도중 오류가 발생했습니다. \n다시 시도해 주세요.");
            }
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/scrap/{meeting_idx}")
    public ResponseEntity<CudResponse> deleteScrap(@RequestHeader (value = "Authorization", required = false) String token, @PathVariable Integer meeting_idx){
        CudResponse response = new CudResponse();
        if(token == null ){
            response.setSuccess(false);
            response.setMessage("로그인 후 이용 가능한 서비스입니다.");
        }else {
            int user_idx = jwtService.getId(token);
            if (meetingService.deleteScrap(meeting_idx, user_idx)) {
                response.setSuccess(true);
                response.setMessage("스크랩이 취소되었습니다.");
            } else {
                response.setSuccess(false);
                response.setMessage("처리 도중 오류가 발생했습니다. \n다시 시도해 주세요.");
            }
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{meeting_idx}/image")
    public ResponseEntity<CudResponse> deleteImage(@PathVariable int meeting_idx,@RequestParam String image){
        CudResponse response = new CudResponse();

        if(meetingService.deleteImage(meeting_idx, image)){
            response.setSuccess(true);
            response.setMessage(("삭제되었습니다"));
        }else{
            response.setSuccess(false);
            response.setMessage(("처리 도중 오류가 발생했습니다. \n다시 시도해 주세요."));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


}

