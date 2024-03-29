package com.gft.prueba.arquitecture.application.mapper;

import com.gft.prueba.arquitecture.domain.model.User;
import com.gft.prueba.arquitecture.domain.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "country", target = "country")
    UserDto toDto(User domain);

}