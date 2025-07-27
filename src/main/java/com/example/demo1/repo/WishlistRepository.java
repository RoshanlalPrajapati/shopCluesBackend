package com.example.demo1.repo;

import com.example.demo1.model.Wishlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

    @Query("SELECT w FROM Wishlist w JOIN FETCH w.user u JOIN FETCH w.product p WHERE u.id = :userId")
    List<Wishlist> findAllByUserId(@Param("userId") Integer userId);

    @Transactional
    void deleteByProduct_Id(Integer productId);

}
