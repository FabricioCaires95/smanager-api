package br.com.smanager.domain.service;

import br.com.smanager.domain.entity.Member;
import br.com.smanager.domain.exception.DuplicateEmailMemberException;
import br.com.smanager.domain.exception.MemberNotFoundException;
import br.com.smanager.domain.repository.MemberRepository;
import br.com.smanager.infrastructure.dto.SaveMemberDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member create(SaveMemberDto memberDto) {
        checkEmailDuplication(memberDto.email(), null);

        var member = Member.builder()
                .secret(UUID.randomUUID().toString())
                .name(memberDto.name())
                .email(memberDto.email())
                .deleted(Boolean.FALSE)
                .build();

        return memberRepository.save(member);
    }

    public Member loadMemberById(String id) {
        return memberRepository
                .findByIdAndDeleted(id, false)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Transactional
    public void deleteMember(String id) {
        var member = loadMemberById(id);

        member.setDeleted(Boolean.TRUE);
    }

    @Transactional
    public Member updateMember(String id, SaveMemberDto memberDto) {
        checkEmailDuplication(memberDto.email(), id);

        var member = loadMemberById(id);
        member.setName(memberDto.name());
        member.setEmail(memberDto.email());

        return member;
    }

    private void checkEmailDuplication(String email, String idToExclude) {
        var isDuplicate = memberRepository.findByEmailAndDeleted(email, Boolean.FALSE)
                .filter(member -> !Objects.equals(member.getId(), idToExclude))
                .isPresent();

        if (isDuplicate) {
            throw new DuplicateEmailMemberException("DuplicateEmail", "Email is duplicated: " + email);
        }
    }


}
