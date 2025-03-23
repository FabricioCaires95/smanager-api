package br.com.smanager.infrastructure.dto;

import br.com.smanager.domain.entity.Member;
import br.com.smanager.domain.entity.Project;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record MemberDto(String id, String secret, String name, String email, Set<String> projects) {

    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getId(),
                member.getSecret(),
                member.getName(),
                member.getEmail(),
                Optional
                        .ofNullable(member.getProjects()).orElse(List.of())
                        .stream()
                        .map(Project::getId)
                        .collect(Collectors.toSet())
        );
    }
}
