package br.com.smanager.infrastructure.controller;

import br.com.smanager.domain.service.MemberService;
import br.com.smanager.infrastructure.dto.MemberDto;
import br.com.smanager.infrastructure.dto.SaveMemberDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

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
        var member = memberService.loadMemberById(id);
        return ResponseEntity.ok(MemberDto.from(member));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<MemberDto> update(@PathVariable String id, @RequestBody @Valid SaveMemberDto memberDto){
        var updatedMember = memberService.updateMember(id, memberDto);
        return ResponseEntity.ok(MemberDto.from(updatedMember));
    }

    @GetMapping
    public ResponseEntity<List<MemberDto>> findMembers(@RequestParam(value = "email", required = false) String email){
        var members = memberService.findMembers(email).stream().map(MemberDto::from).toList();
        return ResponseEntity.ok(members);
    }

}
