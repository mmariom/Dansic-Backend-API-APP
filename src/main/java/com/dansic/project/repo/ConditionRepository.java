package com.dansic.project.repo;

import com.dansic.project.entity.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConditionRepository  extends JpaRepository<Condition, Integer> {

    Condition findById(int id);
}
