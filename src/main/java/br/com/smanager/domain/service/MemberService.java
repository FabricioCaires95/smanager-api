package br.com.smanager.domain.service;

import br.com.smanager.domain.entity.Member;
import br.com.smanager.domain.repository.MemberRepository;
import br.com.smanager.infrastructure.dto.SaveMemberDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member create(SaveMemberDto memberDto) {

        var member = Member.builder()
                .secret(UUID.randomUUID().toString())
                .name(memberDto.name())
                .email(memberDto.email())
                .deleted(Boolean.FALSE)
                .build();

        return memberRepository.save(member);
    }

}
