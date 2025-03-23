package br.com.smanager.infrastructure.dto;

import br.com.smanager.domain.entity.Member;

public record MemberDto(String id, String secret, String name, String email) {

    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getSecret(), member.getName(), member.getEmail());
    }
}
