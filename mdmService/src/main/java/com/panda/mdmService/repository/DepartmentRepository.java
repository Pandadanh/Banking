package com.panda.mdmService.repository;

import com.panda.mdmService.model.Department;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepository extends ReactiveCrudRepository<Department, UUID> {
} 