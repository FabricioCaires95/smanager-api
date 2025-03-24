package br.com.smanager.infrastructure.controller;

import br.com.smanager.domain.service.ProjectService;
import br.com.smanager.domain.service.TaskService;
import br.com.smanager.infrastructure.dto.ProjectDto;
import br.com.smanager.infrastructure.dto.SaveProjectDto;
import br.com.smanager.infrastructure.dto.SaveTaskDto;
import br.com.smanager.infrastructure.dto.TaskDto;
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
import static br.com.smanager.infrastructure.controller.RestConstants.PATH_TASKS;

@RestController
@RequestMapping(PATH_TASKS)
@RequiredArgsConstructor
public class TaskRestResource {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody @Valid SaveTaskDto taskDto){

        var task = taskService.create(taskDto);
        return ResponseEntity.created(URI.create(PATH_PROJECTS + "/" + task.getId())).body(TaskDto.from(task));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable String id){
        var task = taskService.loadById(id);
        return ResponseEntity.ok(TaskDto.from(task));
    }
//
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<Void> delete(@PathVariable String id){
//        taskService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<ProjectDto> update(@PathVariable String id, @RequestBody @Valid SaveProjectDto projectDto){
//        var updatedProject = taskService.updateProject(id, projectDto);
//        return ResponseEntity.ok(ProjectDto.from(updatedProject));
//    }

}
