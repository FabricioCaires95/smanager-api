package br.com.smanager.unittests;

import br.com.smanager.domain.exception.DuplicateEmailMemberException;
import br.com.smanager.domain.exception.MemberNotFoundException;
import br.com.smanager.domain.repository.MemberRepository;
import br.com.smanager.domain.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Optional;

import static br.com.smanager.utils.TestHelper.MEMBER_ID;
import static br.com.smanager.utils.TestHelper.createMember;
import static br.com.smanager.utils.TestHelper.createMemberWithParams;
import static br.com.smanager.utils.TestHelper.createSaveMemberDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MemberServiceTest extends UniteTestConfig {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    public void should_create_member(){
        var savedMember = createMember();

        when(memberRepository.save(any())).thenReturn(savedMember);

        var memberDto = memberService.create(createSaveMemberDto());

        assertNotNull(memberDto);
        assertEquals(savedMember.getId(), memberDto.getId());
        assertEquals(savedMember.getName(), memberDto.getName());
    }

    @Test
    public void should_return_member_by_id(){
        var memberCreated = createMember();

        when(memberRepository.findByIdAndDeleted(anyString(), anyBoolean())).thenReturn(Optional.of(memberCreated));

        var member = memberService.loadMemberById(MEMBER_ID);

        assertNotNull(member);
        assertEquals(MEMBER_ID, member.getId());
        assertEquals(memberCreated.getName(), member.getName());
        assertEquals(memberCreated.getEmail(), member.getEmail());
        assertEquals(memberCreated.getSecret(), member.getSecret());
        assertEquals(memberCreated.getDeleted(), member.getDeleted());
    }

    @Test
    public void should_throw_MemberNotFoundException_for_non_existent_id(){
        when(memberRepository.findByIdAndDeleted(anyString(), anyBoolean())).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.loadMemberById(MEMBER_ID));
    }

    @Test
    public void should_delete_member_by_id(){
        var memberToBeDeleted = createMember();

        when(memberRepository.findByIdAndDeleted(anyString(), anyBoolean())).thenReturn(Optional.of(memberToBeDeleted));

        memberService.deleteMember(MEMBER_ID);

        assertTrue(memberToBeDeleted.getDeleted());
    }

    @Test
    public void should_update_member_by_id(){
        var memberDto = createSaveMemberDto();
        var memberToBeUpdated = createMember();

        when(memberRepository.findByEmailAndDeleted(anyString(), anyBoolean())).thenReturn(Optional.of(memberToBeUpdated));
        when(memberRepository.findByIdAndDeleted(anyString(), anyBoolean())).thenReturn(Optional.of(memberToBeUpdated));

        var member = memberService.updateMember(MEMBER_ID, memberDto);

        assertNotNull(member);
        assertEquals(MEMBER_ID, member.getId());
        assertEquals(memberDto.name(), member.getName());
        assertEquals(memberDto.email(), member.getEmail());
    }

    @Test
    public void should_throw_DuplicateEmailMemberException_for_existent_member(){
        var memberDto = createSaveMemberDto();
        var memberToBeUpdated = createMember();

        when(memberRepository.findByEmailAndDeleted(anyString(), anyBoolean())).thenReturn(Optional.of(memberToBeUpdated));

        assertThrows(DuplicateEmailMemberException.class, () -> memberService.updateMember("TESTE", memberDto));

        verify(memberRepository, times(0)).findByIdAndDeleted(anyString(), anyBoolean());
    }

    @Test
    public void should_return_list_of_active_members_without_email() {
        var spacerMember = createMemberWithParams("id1tttuuu", "Spacer", "spacer@gmail.com", "spacer-secret", false);
        var legendMember = createMemberWithParams("id2xxasas", "Legend", "legend@gmail.com", "legend-secret", false);

        when(memberRepository.findAllNotDeleted()).thenReturn(Arrays.asList(spacerMember, legendMember));

        var membersList = memberService.findMembers(null);

        assertNotNull(membersList);
        assertEquals(2, membersList.size());
        assertFalse(membersList.getFirst().getDeleted());
        assertFalse(membersList.get(1).getDeleted());
    }

    @Test
    public void should_return_list_of_one_active_member_by_email() {
        var spacerMember = createMemberWithParams("id1tttuuu", "Spacer", "spacer@gmail.com", "spacer-secret", false);

        when(memberRepository.findByEmailAndDeleted(anyString(), anyBoolean())).thenReturn(Optional.of(spacerMember));

        var membersList = memberService.findMembers("spacer@gmail.com");

        assertNotNull(membersList);
        assertEquals(1, membersList.size());
        assertFalse(membersList.getFirst().getDeleted());
        assertEquals(spacerMember.getId(), membersList.getFirst().getId());
        assertEquals(spacerMember.getName(), membersList.getFirst().getName());
        assertEquals(spacerMember.getEmail(), membersList.getFirst().getEmail());
        assertEquals(spacerMember.getSecret(), membersList.getFirst().getSecret());
    }

    @Test
    public void should_return_empty_list_() {
        when(memberRepository.findByEmailAndDeleted(anyString(), anyBoolean())).thenReturn(Optional.empty());

        var membersList = memberService.findMembers("anyEmail");

        assertNotNull(membersList);
        assertTrue(membersList.isEmpty());
    }

}
