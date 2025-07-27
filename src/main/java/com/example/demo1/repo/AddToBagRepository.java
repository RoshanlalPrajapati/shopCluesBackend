package com.example.demo1.repo;

import com.example.demo1.model.BagItems;
import com.example.demo1.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddToBagRepository extends JpaRepository<BagItems, Integer> {

    @Query("SELECT b FROM BagItems b WHERE b.user.id = :userId")
    List<BagItems> findByUserId(@Param("userId") Integer userId);

    @Transactional
    void deleteByProduct_Id(Integer productId);
}
