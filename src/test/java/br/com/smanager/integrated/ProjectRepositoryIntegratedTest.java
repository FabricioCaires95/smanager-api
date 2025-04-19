package br.com.smanager.integrated;

import br.com.smanager.domain.entity.Project;
import br.com.smanager.domain.model.ProjectStatus;
import br.com.smanager.domain.repository.ProjectRepository;
import br.com.smanager.utils.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectRepositoryIntegratedTest extends BaseIntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void should_save_a_project_successfully() {
        var project = Project.builder()
                .name("Project FPB")
                .description("Project FPB Description")
                .status(ProjectStatus.PENDING)
                .initialDate(TestHelper.INITIAL_DATE)
                .finalDate(TestHelper.FINAL_DATE)
                .build();

        projectRepository.save(project);

        var savedProject = projectRepository.findProjectByName(project.getName());

        assertTrue(savedProject.isPresent());
        assertNotNull(savedProject.get());
        assertEquals(project.getId(), savedProject.get().getId());
        assertEquals(project.getName(), savedProject.get().getName());
        assertEquals(project.getDescription(), savedProject.get().getDescription());
        assertEquals(project.getStatus(), savedProject.get().getStatus());
    }
}
