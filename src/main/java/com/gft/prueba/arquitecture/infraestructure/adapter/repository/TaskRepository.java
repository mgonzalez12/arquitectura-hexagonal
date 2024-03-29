package com.gft.prueba.arquitecture.infraestructure.adapter.repository;

import com.gft.prueba.arquitecture.infraestructure.adapter.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByIdIn(List<Long> tasksIds);

}