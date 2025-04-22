package br.com.smanager.infrastructure.controller;

import br.com.smanager.domain.service.ProjectService;
import br.com.smanager.infrastructure.dto.ProjectDto;
import br.com.smanager.infrastructure.dto.SaveProjectDto;
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
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static br.com.smanager.infrastructure.controller.RestConstants.PATH_PROJECTS;

@RestController
@RequestMapping(PATH_PROJECTS)
@RequiredArgsConstructor
@Tag(name = "Project Resource")
public class ProjectRestResource {

    private final ProjectService projectService;

    @Operation(description = "Create a new project", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "project created with the corresponding url to retrieve", content = @Content(schema = @Schema(implementation = ProjectDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = RestError.class))),
            @ApiResponse(responseCode = "400", description = "Duplicated project", content = @Content(schema = @Schema(implementation = RestError.class))),
    })
    @PostMapping()
    public ResponseEntity<ProjectDto> create(@RequestBody @Valid SaveProjectDto projectDto){

        var project = projectService.create(projectDto);
        return ResponseEntity.created(URI.create(PATH_PROJECTS + "/" + project.id())).body(project);
    }

    @Operation(description = "Get a project by projectId", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "returned project"),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = RestError.class))),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProjectDto> getById(@PathVariable String id){
        var project = projectService.getById(id);
        return ResponseEntity.ok(ProjectDto.from(project));
    }

    @Operation(description = "Delete a project by projectId", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = RestError.class))),
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "update a project by projectId", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "project successfully updated"),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = RestError.class))),
            @ApiResponse(responseCode = "400", description = "Duplicated project", content = @Content(schema = @Schema(implementation = RestError.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Project Status", content = @Content(schema = @Schema(implementation = RestError.class))),
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProjectDto> update(@PathVariable String id, @RequestBody @Valid SaveProjectDto projectDto){
        var updatedProject = projectService.updateProject(id, projectDto);
        return ResponseEntity.ok(ProjectDto.from(updatedProject));
    }

}
