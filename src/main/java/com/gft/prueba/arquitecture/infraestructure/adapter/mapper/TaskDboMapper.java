package com.gft.prueba.arquitecture.infraestructure.adapter.mapper;

import com.gft.prueba.arquitecture.domain.model.Task;
import com.gft.prueba.arquitecture.infraestructure.adapter.entity.TaskEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskDboMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "completed", target = "completed")
    @Mapping(source = "dateOfCreation", target = "dateOfCreation")
    @Mapping(source = "timeRequiredToComplete", target = "timeRequiredToComplete")
    TaskEntity toDbo(Task domain);

    @InheritInverseConfiguration
    Task toDomain(TaskEntity entity);
}