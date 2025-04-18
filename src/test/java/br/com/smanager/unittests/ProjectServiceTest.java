package br.com.smanager.unittests;

import br.com.smanager.domain.exception.DuplicateProjectException;
import br.com.smanager.domain.exception.InvalidProjectStatusException;
import br.com.smanager.domain.exception.ProjectNotFoundException;
import br.com.smanager.domain.repository.ProjectRepository;
import br.com.smanager.domain.service.MemberService;
import br.com.smanager.domain.service.ProjectService;
import br.com.smanager.infrastructure.dto.SaveProjectDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static br.com.smanager.utils.TestHelper.FINAL_DATE;
import static br.com.smanager.utils.TestHelper.INITIAL_DATE;
import static br.com.smanager.utils.TestHelper.PROJECT_ID;
import static br.com.smanager.utils.TestHelper.createProject;
import static br.com.smanager.utils.TestHelper.createSaveProjectDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProjectServiceTest extends UniteTestConfig {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MemberService memberService;

    @Test
    public void should_create_a_project() {
        var saveProjectDto = createSaveProjectDto();
        var projectEntity = createProject();

        when(projectRepository.findProjectByName(anyString())).thenReturn(Optional.empty());
        when(projectRepository.save(any())).thenReturn(projectEntity);

        var projectDto = projectService.create(saveProjectDto);

        assertNotNull(projectDto);
        assertNotNull(projectDto.id());
        assertNotNull(projectDto.name());
        assertNotNull(projectDto.description());
        assertNotNull(projectDto.initialDate());
        assertNotNull(projectDto.finalDate());
        assertNotNull(projectDto.status());
    }

    @Test
    public void should_reject_creating_duplicate_project() {
        var saveProjectDto = createSaveProjectDto();

        var project = createProject();

        when(projectRepository.findProjectByName(anyString())).thenReturn(Optional.of(project));

        assertThrows(DuplicateProjectException.class, () -> projectService.create(saveProjectDto));

        verify(projectRepository, times(0)).save(project);
    }

    @Test
    public void should_get_project_by_id_successfully() {
        var projectResult = createProject();

        when(projectRepository.findById(anyString())).thenReturn(Optional.of(projectResult));

        var project = projectService.getById("teadadas");

        assertNotNull(project);
        assertEquals(projectResult.getName(), project.getName());
        assertEquals(projectResult.getId(), project.getId());
        assertEquals(projectResult.getDescription(), project.getDescription());
        assertEquals(projectResult.getInitialDate(), project.getInitialDate());
    }

    @Test
    public void should_throw_ProjectNotFoundException() {
        when(projectRepository.findById(anyString())).thenReturn(Optional.empty());

        var projectNotFoundException = assertThrows(ProjectNotFoundException.class, () -> projectService.getById(PROJECT_ID));

        var expectedMessage = "Project Not found: " + PROJECT_ID;

        assertTrue(projectNotFoundException.getMessage().contains(expectedMessage));
    }

    @Test
    public void should_delete_project_by_id_successfully() {
        var project = createProject();

        when(projectRepository.findById(anyString())).thenReturn(Optional.of(project));

        projectService.delete(PROJECT_ID);

        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    public void should_update_project_by_id_successfully() {
        var saveProjectDto = createSaveProjectDto();
        var projectEntity = createProject();

        when(projectRepository.findProjectByName(anyString())).thenReturn(Optional.empty());
        when(projectRepository.findById(anyString())).thenReturn(Optional.of(projectEntity));

        var projectUpdated = projectService.updateProject(PROJECT_ID, createSaveProjectDto());

        assertNotNull(projectUpdated);
        assertEquals(saveProjectDto.name(), projectUpdated.getName());
        assertEquals(saveProjectDto.description(), projectUpdated.getDescription());
        assertEquals(saveProjectDto.initialDate(), projectUpdated.getInitialDate());
        assertEquals(saveProjectDto.finalDate(), projectUpdated.getFinalDate());
    }

    @Test
    public void should_throw_InvalidProjectStatusException_for_invalid_project_status() {
        var projectEntity = createProject();

        var invalidProjectStatus = "ANYTHING";

        when(projectRepository.findById(anyString())).thenReturn(Optional.of(projectEntity));

        var saveProjectDtoInvalidStatus = new SaveProjectDto(
                "test",
                "descript",
                INITIAL_DATE,
                FINAL_DATE,
                invalidProjectStatus,
                null);

        var invalidProjectStatusException = assertThrows(InvalidProjectStatusException.class,
                () -> projectService.updateProject(PROJECT_ID, saveProjectDtoInvalidStatus));

        var expectedMessage = "Invalid project status: " + invalidProjectStatus;

        assertTrue(invalidProjectStatusException.getMessage().contains(expectedMessage));
    }

}
