package br.com.smanager.infrastructure.controller;

import br.com.smanager.domain.service.MemberService;
import br.com.smanager.infrastructure.dto.MemberDto;
import br.com.smanager.infrastructure.dto.SaveMemberDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static br.com.smanager.infrastructure.controller.RestConstants.PATH_MEMBER;

@RestController
@RequestMapping(value = PATH_MEMBER)
@RequiredArgsConstructor
public class MemberRestResource {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberDto> create(@RequestBody @Valid SaveMemberDto memberDto){

        var member = memberService.create(memberDto);
        return ResponseEntity.created(URI.create(PATH_MEMBER + "/" + member.getId())).body(MemberDto.from(member));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MemberDto> getById(@PathVariable String id){
        var member = memberService.findById(id);
        return ResponseEntity.ok(MemberDto.from(member));
    }
}
