package com.example.unimeeting.controller;

import com.example.unimeeting.domain.User;
import com.example.unimeeting.dto.CudResponse;
import com.example.unimeeting.dto.MeetingWithDetailsDTO;
import com.example.unimeeting.service.JwtService;
import com.example.unimeeting.service.JwtServiceImpl;
import com.example.unimeeting.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor()
@RequestMapping("/mypage")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*",exposedHeaders = "Authorization", allowCredentials = "true")
public class MypageController {

    private final MypageService service;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "모임 리스트를 출력")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "참여 모임 리스트",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = MeetingWithDetailsDTO.class)) })
    })
    @GetMapping("/meetings/participated")
    public ResponseEntity<List<MeetingWithDetailsDTO>> attendList(@RequestHeader(value = "Authorization", required = false) String token) {
        ResponseEntity<List<MeetingWithDetailsDTO>> entity = new ResponseEntity<>(service.joinMeeting(
            jwtService.getId(token)), HttpStatus.OK);

        return entity;
    }

    @Operation(summary = "모임 리스트를 출력")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "생성 모임 리스트",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = MeetingWithDetailsDTO.class)) })
    })
    @GetMapping("/meetings/created")
    public ResponseEntity<List<MeetingWithDetailsDTO>> createList(@RequestHeader(value = "Authorization", required = false) String token) {
        ResponseEntity<List<MeetingWithDetailsDTO>> entity = new ResponseEntity<>(service.createMeeting(jwtService.getId(token)), HttpStatus.OK);
        return entity;
    }

    @Operation(summary = "모임 리스트를 출력")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "스크랩 모임 리스트",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = MeetingWithDetailsDTO.class)) })
    })
    @GetMapping("/meetings/scraped")
    public ResponseEntity<List<MeetingWithDetailsDTO>> scrapList(@RequestHeader(value = "Authorization", required = false) String token) {
        ResponseEntity<List<MeetingWithDetailsDTO>> entity = new ResponseEntity<>(service.scrapMeeting(jwtService.getId(token)), HttpStatus.OK);
        return entity;
    }

    @Operation(summary = "유저 Update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Update 정보",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = MeetingWithDetailsDTO.class)) })
    })
    @PutMapping("/user")
    public ResponseEntity<CudResponse> updateUser(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody User user){
        CudResponse response = new CudResponse();
        int user_idx = jwtService.getId(token);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean result = service.updateUser(user, user_idx);
        if (result){
            response.setSuccess(true);
            response.setMessage("정보 변경이 완료되었습니다! \n 다시 로그인 해주세요~!");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            response.setSuccess(false);
            response.setMessage("처리 도중 오류가 발생했습니다. \n다시 시도해 주세요.");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "유저 Delete")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delete 정보",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = MeetingWithDetailsDTO.class)) })
    })
    @DeleteMapping("/user")
    public ResponseEntity<CudResponse> deleteUser(@RequestHeader(value = "Authorization", required = false) String token, @RequestParam String password){
        CudResponse response = new CudResponse();
        int user_idx = jwtService.getId(token);
        User user = service.findUser(user_idx);
        System.out.println("+".repeat(80) + password);

        if(passwordEncoder.matches(password, user.getPassword())) {
            boolean result = service.deleteUser(user_idx);
            if (result) {
                response.setSuccess(true);
                response.setMessage("저희 서비스를 이용해 주셔서 감사합니다!");
            } else {
                response.setSuccess(false);
                response.setMessage("처리 도중 오류가 발생했습니다. \n다시 시도해 주세요.");
            }
        } else {
            System.out.println("비번 일치 x");
            response.setSuccess(false);
            response.setMessage("비밀번호가 일치하지 않습니다. \n다시 확인해 주세요.");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
