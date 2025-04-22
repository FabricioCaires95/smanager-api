package br.com.smanager.infrastructure.controller;

import br.com.smanager.domain.service.TaskService;
import br.com.smanager.infrastructure.dto.SaveTaskDto;
import br.com.smanager.infrastructure.dto.TaskDto;
import br.com.smanager.infrastructure.exception.RestError;
import br.com.smanager.infrastructure.util.SortProperties;
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
@Tag(name = "Task Resource")
public class TaskRestResource {

    private final TaskService taskService;

    @Operation(description = "Create a new Task", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created with the corresponding url to retrieve", content = @Content(schema = @Schema(implementation = TaskDto.class))),
    })
    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody @Valid SaveTaskDto taskDto){

        var task = taskService.create(taskDto);
        return ResponseEntity.created(URI.create(PATH_PROJECTS + "/" + task.getId())).body(TaskDto.from(task));
    }

    @Operation(description = "Get a task by Id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task returns"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(schema = @Schema(implementation = RestError.class))),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable String id){
        var task = taskService.loadById(id);
        return ResponseEntity.ok(TaskDto.from(task));
    }

    @Operation(description = "Delete a task by Id", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(schema = @Schema(implementation = RestError.class))),
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "update a task by Id", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(schema = @Schema(implementation = RestError.class))),
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable String id, @RequestBody @Valid SaveTaskDto taskDto){
        var updatedTask = taskService.update(id, taskDto);
        return ResponseEntity.ok(TaskDto.from(updatedTask));
    }

    @Operation(description = "Get a task by parameters", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content(schema = @Schema(implementation = RestError.class))),
            @ApiResponse(responseCode = "400", description = "Task Invalid status", content = @Content(schema = @Schema(implementation = RestError.class))),
    })
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
