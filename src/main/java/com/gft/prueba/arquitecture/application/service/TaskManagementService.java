package com.gft.prueba.arquitecture.application.service;

import com.gft.prueba.arquitecture.application.mapper.TaskDtoMapper;
import com.gft.prueba.arquitecture.application.mapper.TaskRequestMapper;
import com.gft.prueba.arquitecture.application.usecases.TaskService;
import com.gft.prueba.arquitecture.domain.model.constant.TaskConstant;
import com.gft.prueba.arquitecture.domain.model.dto.TaskDto;
import com.gft.prueba.arquitecture.domain.model.dto.request.TaskRequest;
import com.gft.prueba.arquitecture.domain.port.TaskPersistencePort;
import com.gft.prueba.arquitecture.infraestructure.adapter.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskManagementService implements TaskService {

    private final TaskPersistencePort taskPersistencePort;
    private final TaskRequestMapper taskRequestMapper;
    private final TaskDtoMapper taskDtoMapper;

    @Autowired
    public TaskManagementService(TaskPersistencePort taskPersistencePort,
                                 TaskRequestMapper taskRequestMapper,
                                 TaskDtoMapper taskDtoMapper
    ) {
        this.taskPersistencePort = taskPersistencePort;
        this.taskRequestMapper = taskRequestMapper;
        this.taskDtoMapper = taskDtoMapper;
    }

    @Override
    public TaskDto createNew(TaskRequest request) {
        var taskToCreate = taskRequestMapper.toDomain(request);
        var taskCreated = taskPersistencePort.create(taskToCreate);
        return taskDtoMapper.toDto(taskCreated);
    }

    @Override
    public TaskDto getById(Long id) {
        return taskDtoMapper.toDto(taskPersistencePort.getById(id));
    }

    @Override
    public List<TaskDto> getAll() {
        return taskPersistencePort.getAll()
                .stream()
                .map(taskDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        var task = taskPersistencePort.getById(id);
        if(!task.getUsers().isEmpty()){
            throw  new UserException(HttpStatus.BAD_REQUEST,
                    String.format(TaskConstant.CURRENT_TASK_NOT_ALLOW_TO_DELETE, task.getId()));
        }
        taskPersistencePort.deleteById(id);
    }

    @Override
    public TaskDto update(TaskRequest request, Long id) {
        var taskToUpdate = taskRequestMapper.toDomain(request);
        taskToUpdate.setName(request.getName());
        taskToUpdate.setDescription(request.getDescription());
        taskToUpdate.setTimeRequiredToComplete(request.getTimeRequiredToComplete());
        return taskDtoMapper.toDto(taskPersistencePort.update(taskToUpdate));
    }
}
