package br.com.smanager.infrastructure.controller;

import br.com.smanager.domain.service.TaskService;
import br.com.smanager.infrastructure.dto.SaveTaskDto;
import br.com.smanager.infrastructure.dto.TaskDto;
import br.com.smanager.infrastructure.util.SortProperties;
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
import java.util.Optional;

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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable String id, @RequestBody @Valid SaveTaskDto taskDto){
        var updatedTask = taskService.update(id, taskDto);
        return ResponseEntity.ok(TaskDto.from(updatedTask));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> findTasks(
            @RequestParam(name = "projectId", required = false) String projectId,
            @RequestParam(name = "memberId", required = false) String memberId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "partialTitle", required = false) String partialTitle,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "direction", required = false) String direction,
            @RequestParam(name = "sort", required = false) SortProperties propeties
    ) {

        var tasks = taskService.findTasks(
                projectId,
                memberId,
                status,
                partialTitle,
                page,
                direction,
                Optional.ofNullable(propeties).map(SortProperties::getSortPropertiesList).orElse(List.of()));

        return ResponseEntity.ok(tasks.stream().map(TaskDto::from).toList());
    }

}
