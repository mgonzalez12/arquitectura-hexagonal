package com.gft.prueba.arquitecture.application.service;

import com.gft.prueba.arquitecture.application.mapper.UserDtoMapper;
import com.gft.prueba.arquitecture.application.mapper.UserRequestMapper;
import com.gft.prueba.arquitecture.application.usecases.UserService;
import com.gft.prueba.arquitecture.domain.model.Task;
import com.gft.prueba.arquitecture.domain.model.constant.UserConstant;
import com.gft.prueba.arquitecture.domain.model.dto.UserDto;
import com.gft.prueba.arquitecture.domain.model.dto.request.UserRequest;
import com.gft.prueba.arquitecture.domain.port.TaskPersistencePort;
import com.gft.prueba.arquitecture.domain.port.UserPersistencePort;
import com.gft.prueba.arquitecture.infraestructure.adapter.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementService implements UserService {

    private final UserPersistencePort userPersistencePort;
    private final TaskPersistencePort taskPersistencePort;
    private final UserRequestMapper userRequestMapper;
    private final UserDtoMapper userDtoMapper;

    @Autowired
    public UserManagementService(UserPersistencePort userPersistencePort,
                                 TaskPersistencePort taskPersistencePort,
                                 UserRequestMapper userRequestMapper,
                                 UserDtoMapper userDtoMapper) {
        this.userPersistencePort = userPersistencePort;
        this.taskPersistencePort = taskPersistencePort;
        this.userRequestMapper = userRequestMapper;
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public UserDto createNew(UserRequest request) {
        return userDtoMapper.toDto(userPersistencePort.create(userRequestMapper.toDomain(request)));
    }

    @Override
    public UserDto getById(Long id) {
        return userDtoMapper.toDto(userPersistencePort.getById(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userPersistencePort.getAll()
                .stream()
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        var user = userPersistencePort.getById(id);
        if(!user.getTasks().isEmpty()){
            throw new UserException(HttpStatus.BAD_REQUEST,
                    String.format(UserConstant.CURRENT_USER_NOT_ALLOW_TO_DELETE, user.getName()));
        }
        userPersistencePort.deleteById(id);
    }

    @Override
    public UserDto update(UserRequest request, Long id) {
        var userToUpdate = userRequestMapper.toDomain(request);
        userToUpdate.setName(request.getName());
        userToUpdate.setAge(request.getAge());
        userToUpdate.setCountry(request.getCountry());
        return userDtoMapper.toDto(userPersistencePort.update(userToUpdate));
    }

    @Override
    public void assignTasks(Long id, List<Long> tasksIds) {
        var user = userPersistencePort.getById(id);
        var tasksToDo = taskPersistencePort.getByIds(tasksIds);
        validateAvailabilityToAssign(new ArrayList<>(user.getTasks()), tasksToDo);
    }

    private void validateAvailabilityToAssign(List<Task> currentTasks, List<Task> tasksToDo) {
        int totalTimeInTasks = getRequirementTimeToDo(currentTasks, tasksToDo);
        var isNotAllow = totalTimeInTasks > 8;
        if (isNotAllow) {
            throw new UserException(HttpStatus.BAD_REQUEST, String.format(UserConstant.CURRENT_USER_NOT_ALLOW_TO_DO_TASKS,
                    totalTimeInTasks));
        }
    }

    private int getRequirementTimeToDo(List<Task> currentTasks, List<Task> tasksToDo) {

        var timeInCurrentTasks = currentTasks.stream()
                .mapToInt(Task::getTimeRequiredToComplete)
                .sum();

        var timeInTasksToDo = tasksToDo.stream()
                .mapToInt(Task::getTimeRequiredToComplete)
                .sum();

        return timeInCurrentTasks + timeInTasksToDo;
    }
}
