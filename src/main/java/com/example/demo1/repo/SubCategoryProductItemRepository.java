package com.example.demo1.repo;

import com.example.demo1.model.subCategoryData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryProductItemRepository extends JpaRepository<subCategoryData, Integer> {
    List<subCategoryData> findByHeaderAndSubCategory(String header, String subCategory);
}
