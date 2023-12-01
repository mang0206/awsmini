package com.example.unimeeting.dto;

import com.example.unimeeting.domain.Member;
import com.example.unimeeting.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddMemberRequest {
    private Integer meetingIdx;
    private User user;

    public Member toEntity(){
        return Member.builder()
                .meetingIdx(meetingIdx)
                .user(user)
                .build();
    }

}
