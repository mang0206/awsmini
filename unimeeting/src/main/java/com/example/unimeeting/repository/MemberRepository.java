package com.example.unimeeting.repository;

import com.example.unimeeting.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    public int countByMeetingIdx(int idx);

    public boolean existsByMeetingIdxAndUserIdx(int meeting_idx, int user_idx);

    public void deleteByMeetingIdxAndUserIdx(int meeting_idx, int user_idx);

    public List<Member> findByMeetingIdx(int meeting_idx);

    public Member findByMeetingIdxAndUserIdx(int meeting_idx, int user_idx);

}
