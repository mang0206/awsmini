package com.example.unimeeting.dto;

import com.example.unimeeting.domain.Meeting;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateMeetingResponse {
    private Meeting meeting;
    private List<String> imgUrl;
}
