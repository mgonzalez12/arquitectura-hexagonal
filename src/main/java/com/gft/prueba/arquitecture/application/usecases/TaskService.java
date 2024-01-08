package com.gft.prueba.arquitecture.application.usecases;

import com.gft.prueba.arquitecture.domain.model.dto.TaskDto;
import com.gft.prueba.arquitecture.domain.model.dto.request.TaskRequest;

import java.util.List;

public interface TaskService {

    TaskDto createNew(TaskRequest request);
    TaskDto getById(Long id);
    List<TaskDto> getAll();
    void deleteById(Long id);
    TaskDto update(TaskRequest request, Long id);

}
