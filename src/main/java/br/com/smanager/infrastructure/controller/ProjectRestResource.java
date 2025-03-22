package br.com.smanager.infrastructure.controller;

import br.com.smanager.domain.service.ProjectService;
import br.com.smanager.infrastructure.dto.ProjectDto;
import br.com.smanager.infrastructure.dto.SaveProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static br.com.smanager.infrastructure.controller.RestConstants.PATH_PROJECTS;

@RestController
@RequestMapping(PATH_PROJECTS)
@RequiredArgsConstructor
public class ProjectRestResource {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDto> create(@RequestBody SaveProjectDto projectDto){

        var project = projectService.create(projectDto);
        return ResponseEntity.created(URI.create(PATH_PROJECTS + "/" + project.id())).body(project);
    }

    @GetMapping
    public ResponseEntity<ProjectDto> getById(@PathVariable String id){
        return ResponseEntity.ok(projectService.getById(id));
    }

}
