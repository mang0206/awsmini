package com.example.unimeeting.dto;

import com.example.unimeeting.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberResponse {
    private int idx;
    private int userIdx;
    private String nickname;
    private String category;
    private boolean accepted;

    public MemberResponse(Member member){
        this.idx = member.getIdx();
        this.userIdx = member.getUser().getIdx();
        this.nickname = member.getUser().getNickname();
        this.category = member.getUser().getCategory();
        this.accepted = member.isAccepted();
    }
}
