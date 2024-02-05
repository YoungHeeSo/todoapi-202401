package com.study.todoapi.side.repository;

import com.study.todoapi.side.entity.SideMenuImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SideMenuImageRepository extends JpaRepository<SideMenuImage, Integer> {

    @Query("SELECT COUNT(*) FROM SideMenuImage s")
    int countSideMenuImagesByAll();

    Optional<SideMenuImage> findByImgId(int id);

}
