package br.com.smanager.integrated;

import br.com.smanager.domain.entity.Member;
import br.com.smanager.domain.entity.Project;
import br.com.smanager.domain.entity.Task;
import br.com.smanager.domain.model.ProjectStatus;
import br.com.smanager.domain.model.TaskStatus;
import br.com.smanager.domain.repository.MemberRepository;
import br.com.smanager.domain.repository.ProjectRepository;
import br.com.smanager.domain.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static br.com.smanager.utils.TestHelper.FINAL_DATE;
import static br.com.smanager.utils.TestHelper.INITIAL_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TaskRepositoryIntegratedTest extends BaseIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void should_create_task_and_assign_project_and_assign_member() {
        var project = Project.builder()
                .name("Project X")
                .status(ProjectStatus.PENDING)
                .description("Description for Project X")
                .initialDate(INITIAL_DATE)
                .finalDate(FINAL_DATE)
                .build();
        projectRepository.save(project);

        var member = Member.builder()
                .name("Spacer X")
                .email("spacer@gmail.com")
                .deleted(false)
                .secret("anySecret")
                .projects(List.of(project))
                .build();

        memberRepository.save(member);

        var taskToBeCreated = Task.builder()
                .title("Task Title A")
                .description("Task Description A")
                .numberOfDays(5)
                .status(TaskStatus.IN_PROGRESS)
                .assignedMember(member)
                .project(project)
                .build();

        taskRepository.save(taskToBeCreated);

        var result = taskRepository.findById(taskToBeCreated.getId());

        assertTrue(result.isPresent());
        assertEquals(taskToBeCreated.getTitle(), result.get().getTitle());
        assertEquals(taskToBeCreated.getDescription(), result.get().getDescription());
        assertEquals(taskToBeCreated.getNumberOfDays(), result.get().getNumberOfDays());
        assertEquals(taskToBeCreated.getStatus(), result.get().getStatus());
    }

}
