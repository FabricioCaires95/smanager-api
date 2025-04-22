package br.com.smanager.infrastructure.controller;

import br.com.smanager.domain.service.MemberService;
import br.com.smanager.infrastructure.dto.MemberDto;
import br.com.smanager.infrastructure.dto.SaveMemberDto;
import br.com.smanager.infrastructure.exception.RestError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Member rest resource")
public class MemberRestResource {

    private final MemberService memberService;

    @Operation(description = "Create a new member", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member created with the corresponding url to retrieve", content = @Content(schema = @Schema(implementation = MemberDto.class))),
            @ApiResponse(responseCode = "400", description = "Email already exists", content = @Content(schema = @Schema(implementation = MemberDto.class))),
    })
    @PostMapping
    public ResponseEntity<MemberDto> create(@RequestBody @Valid SaveMemberDto memberDto){

        var member = memberService.create(memberDto);
        return ResponseEntity.created(URI.create(PATH_MEMBER + "/" + member.getId())).body(MemberDto.from(member));
    }

    @Operation(description = "Get a member by id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OK", content = @Content(schema = @Schema(implementation = MemberDto.class))),
            @ApiResponse(responseCode = "404", description = "Member not found", content = @Content(schema = @Schema(implementation = MemberDto.class))),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<MemberDto> getById(@PathVariable String id){
        var member = memberService.loadMemberById(id);
        return ResponseEntity.ok(MemberDto.from(member));
    }

    @Operation(description = "Delete a Member by Id", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Member successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Member not found", content = @Content(schema = @Schema(implementation = RestError.class))),
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "update a Member by Id", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Member not found", content = @Content(schema = @Schema(implementation = RestError.class))),
            @ApiResponse(responseCode = "400", description = "Email Member already exists", content = @Content(schema = @Schema(implementation = MemberDto.class))),
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<MemberDto> update(@PathVariable String id, @RequestBody @Valid SaveMemberDto memberDto){
        var updatedMember = memberService.updateMember(id, memberDto);
        return ResponseEntity.ok(MemberDto.from(updatedMember));
    }

    @Operation(description = "Get a list of members", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MemberDto.class))),
    })
    @GetMapping
    public ResponseEntity<List<MemberDto>> findMembers(@RequestParam(value = "email", required = false) String email){
        var members = memberService.findMembers(email).stream().map(MemberDto::from).toList();
        return ResponseEntity.ok(members);
    }

}
