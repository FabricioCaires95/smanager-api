package br.com.smanager.utils;

import br.com.smanager.domain.document.ApiKey;
import br.com.smanager.domain.entity.Member;
import br.com.smanager.domain.entity.Project;
import br.com.smanager.domain.entity.Task;
import br.com.smanager.domain.model.ProjectStatus;
import br.com.smanager.domain.model.TaskStatus;
import br.com.smanager.infrastructure.dto.SaveMemberDto;
import br.com.smanager.infrastructure.dto.SaveProjectDto;
import br.com.smanager.infrastructure.dto.SaveTaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public final class TestHelper {

    public static final String PROJECT_ID = "sadasdadasad4s5d2454";
    public static final String MEMBER_ID = "medadasad7s5d1084";
    public static final String TASK_ID = "tasdadasad8s5d11079";
    public static final String SECRET_ID = "secretadasad8s5d15091";
    public static final String API_KEY_ID = "apikeysad8s5d156091";
    public static final String API_KEY_TEST = "testApiKey";
    public static final LocalDate INITIAL_DATE = LocalDate.of(2025, 10, 1);
    public static final LocalDate FINAL_DATE = LocalDate.of(2025, 10, 10);


    private TestHelper() {}


    public static Project createProject() {
        return Project.builder()
                .id(PROJECT_ID)
                .status(ProjectStatus.PENDING)
                .initialDate(INITIAL_DATE)
                .finalDate(FINAL_DATE)
                .name("Project Z")
                .description("Test description for project Z")
                .build();
    }

    public static Task createTask() {
        return Task.builder()
                .id(TASK_ID)
                .title("Test title for task Z")
                .description("Test description for task Z")
                .status(TaskStatus.IN_PROGRESS)
                .numberOfDays(5)
                .assignedMember(createMember())
                .project(createProject())
                .build();
    }

    public static SaveTaskDto createTaskDto() {
        return new SaveTaskDto(
                "Task X title",
                "Descript for task X",
                5,
                TaskStatus.PENDING,
                PROJECT_ID,
                MEMBER_ID);
    }

    public static Member createMember() {
        return Member.builder()
                .id(MEMBER_ID)
                .name("Jhow Dix")
                .email("ditx754@gmail.com")
                .secret(SECRET_ID)
                .deleted(false)
                .build();
    }

    public static Member createMemberWithParams(String id, String name, String email, String secret, boolean deleted) {
        return Member.builder()
                .id(id)
                .name(name)
                .email(email)
                .secret(SECRET_ID)
                .deleted(deleted)
                .build();
    }

    public static SaveProjectDto createSaveProjectDto() {
        return new SaveProjectDto(
                "Project X",
                "Desc for project x",
                INITIAL_DATE,
                FINAL_DATE,
                "PENDING",
                null
        );
    }

    public static ApiKey getApiKey() {
        return ApiKey.builder()
                .id(API_KEY_ID)
                .creationWhen(Instant.MIN)
                .expirationWhen(Instant.MAX)
                .value(API_KEY_TEST)
                .build();
    }

    public static SaveMemberDto createSaveMemberDto() {
        return new SaveMemberDto("Spacer Test", "spacer.test@gmail.com");
    }

    public static Page<Task> getPageableTasks() {
        return new PageImpl<>(List.of(createTask(), createTask()));

    }
}
