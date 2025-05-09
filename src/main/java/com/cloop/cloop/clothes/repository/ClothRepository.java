package com.cloop.cloop.clothes.repository;

import com.cloop.cloop.auth.domain.User;
import com.cloop.cloop.clothes.domain.Cloth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothRepository extends JpaRepository<Cloth, Long> {
    List<Cloth> findByUser(User user);
}