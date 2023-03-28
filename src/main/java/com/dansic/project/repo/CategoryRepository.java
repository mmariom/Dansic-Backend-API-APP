package com.dansic.project.repo;

import com.dansic.project.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

    Category findById(Long id);

}
