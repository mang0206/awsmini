package com.example.unimeeting.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="meeting_image")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MeetingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(name = "image_url")
    private String imageUrl;

//    @OneToOne
//    @JoinColumn(name = "meeting_idx")
    @Column(name = "meeting_idx")
    private Integer meetingIdx;

    @Builder
    public MeetingImage(String imageUrl, Integer meetingIdx){
        this.imageUrl = imageUrl;
        this.meetingIdx = meetingIdx;
    }
}
