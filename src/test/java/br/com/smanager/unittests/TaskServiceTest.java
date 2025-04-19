package br.com.smanager.unittests;

import br.com.smanager.domain.exception.InvalidTaskStatusException;
import br.com.smanager.domain.exception.TaskNotFoundException;
import br.com.smanager.domain.model.TaskStatus;
import br.com.smanager.domain.repository.TaskRepository;
import br.com.smanager.domain.service.MemberService;
import br.com.smanager.domain.service.ProjectService;
import br.com.smanager.domain.service.TaskService;
import br.com.smanager.infrastructure.config.AppConfigProperties;
import br.com.smanager.infrastructure.dto.SaveTaskDto;
import br.com.smanager.utils.TestHelper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static br.com.smanager.utils.TestHelper.MEMBER_ID;
import static br.com.smanager.utils.TestHelper.PROJECT_ID;
import static br.com.smanager.utils.TestHelper.TASK_ID;
import static br.com.smanager.utils.TestHelper.createMember;
import static br.com.smanager.utils.TestHelper.createProject;
import static br.com.smanager.utils.TestHelper.createTask;
import static br.com.smanager.utils.TestHelper.createTaskDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TaskServiceTest extends UniteTestConfig {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private MemberService memberService;

    @Mock
    private AppConfigProperties appConfigProperties;

    @Mock
    private AppConfigProperties.General generalProperties;


    @Test
    public void should_create_task_successfully() {
        var project = createProject();
        var member = createMember();
        var task = createTask();
        SaveTaskDto saveTaskDto = createTaskDto();


        when(projectService.getById(anyString())).thenReturn(project);
        when(memberService.loadMemberById(anyString())).thenReturn(member);
        when(taskRepository.save(any())).thenReturn(task);

        var savedTask = taskService.create(saveTaskDto);

        assertNotNull(savedTask);
        assertNotNull(savedTask.getProject());
        assertNotNull(savedTask.getAssignedMember());
        assertEquals(task.getId(), savedTask.getId());
        assertEquals(task.getStatus(), savedTask.getStatus());
        assertEquals(task.getTitle(), savedTask.getTitle());
        assertEquals(task.getNumberOfDays(), savedTask.getNumberOfDays());
    }

    @Test
    public void should_load_task_by_id_successfully() {
        var task = createTask();

        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));

        var loadedTask = taskService.loadById(TASK_ID);

        assertNotNull(loadedTask);
        assertEquals(TASK_ID, loadedTask.getId());
        assertEquals(task.getTitle(), loadedTask.getTitle());
        assertEquals(task.getDescription(), loadedTask.getDescription());
        assertEquals(task.getNumberOfDays(), loadedTask.getNumberOfDays());
        assertEquals(task.getStatus().name(), loadedTask.getStatus().name());
    }

    @Test
    public void should_thrown_TaskNotFoundException_for_non_existent_task() {
        when(taskRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.loadById(TASK_ID));
    }

    @Test
    public void should_update_task_data_successfully_by_id() {
        var project = createProject();
        var member = createMember();
        SaveTaskDto saveTaskDto = createTaskDto();
        var task = createTask();

        when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));
        when(projectService.getById(anyString())).thenReturn(project);
        when(memberService.loadMemberById(anyString())).thenReturn(member);

        var updatedTask = taskService.update(TASK_ID, saveTaskDto);

        assertNotNull(updatedTask);
        assertEquals(saveTaskDto.description(), updatedTask.getDescription());
        assertEquals(saveTaskDto.title(), updatedTask.getTitle());
        assertEquals(saveTaskDto.numberOfDays(), updatedTask.getNumberOfDays());
        assertEquals(saveTaskDto.status().name(), updatedTask.getStatus().name());
        assertEquals(saveTaskDto.projectId(), updatedTask.getProject().getId());
        assertEquals(saveTaskDto.memberId(), updatedTask.getAssignedMember().getId());
    }

    @Test
    public void should_return_page_of_tasks_successfully() {
        var pageTask = TestHelper.getPageableTasks();

        when(appConfigProperties.getGeneral()).thenReturn(generalProperties);
        when(generalProperties.getPageSize()).thenReturn(2);
        when(taskRepository.find(anyString(), anyString(), any(), anyString(), any())).thenReturn(pageTask);

        var pages = taskService.findTasks(PROJECT_ID, MEMBER_ID, TaskStatus.PENDING.name(), "title", 2, "asc", List.of());

        assertNotNull(pages);
        assertEquals(pageTask.getTotalElements(), pages.getTotalElements());
        assertEquals(pageTask.getTotalPages(), pages.getTotalPages());
        assertEquals(pageTask.getNumberOfElements(), pages.getNumberOfElements());
    }

    @Test
    public void should_thrown_InvalidTaskStatusException_when_task_status_is_invalid() {
        assertThrows(InvalidTaskStatusException.class,
                ()-> taskService.findTasks(PROJECT_ID, MEMBER_ID, "invalid_status", "title", 2, "asc", List.of()));
    }


}
