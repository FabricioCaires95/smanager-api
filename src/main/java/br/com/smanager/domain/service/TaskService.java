package br.com.smanager.domain.service;

import br.com.smanager.domain.entity.Task;
import br.com.smanager.domain.exception.TaskNotFoundException;
import br.com.smanager.domain.model.TaskStatus;
import br.com.smanager.domain.repository.TaskRepository;
import br.com.smanager.infrastructure.dto.SaveTaskDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public Task create(SaveTaskDto taskDto) {
        var task = Task.builder()
                .title(taskDto.title())
                .description(taskDto.description())
                .numberOfDays(taskDto.numberOfDays())
                .status(TaskStatus.PENDING)
                .build();

        return taskRepository.save(task);
    }

    public Task loadById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public void deleteTask(String id) {
        var task = loadById(id);
        taskRepository.delete(task);
    }

    @Transactional
    public Task update(String id, SaveTaskDto taskDto) {
        var task = loadById(id);

        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setNumberOfDays(taskDto.numberOfDays());
        task.setStatus(taskDto.status());

        return task;
    }
}

