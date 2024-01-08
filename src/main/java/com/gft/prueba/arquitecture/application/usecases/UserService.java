package com.gft.prueba.arquitecture.application.usecases;

import com.gft.prueba.arquitecture.domain.model.dto.UserDto;
import com.gft.prueba.arquitecture.domain.model.dto.request.UserRequest;

import java.util.List;

public interface UserService {
    UserDto createNew(UserRequest request);
    UserDto getById(Long id);
    List<UserDto> getAll();
    void deleteById(Long id);
    UserDto update(UserRequest request, Long id);
    void assignTasks(Long id, List<Long> tasksIds);
}
