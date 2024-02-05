package com.study.todoapi.side.service;


import com.study.todoapi.side.entity.SideMenuImage;
import com.study.todoapi.side.repository.SideMenuImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class SideService {

    private final SideMenuImageRepository sideMenuImageRepository;

    @Value("${sideMenu.path}")
    private String sideMenuPath;

    // ========== side menu image ==========

    // 이미지 랜덤으로 뽑기
    public int randomImageId(){

        int count = sideMenuImageRepository.countSideMenuImagesByAll();

        Random ranId = new Random();
        int ran = ranId.nextInt(count)+1;

        log.info("ran: {}", ran);

        return ran;
    }

    // 랜덤으로 뽑힌 이미지 경로 조회
    public String getMenuImgPath(){
        int id = randomImageId();

        SideMenuImage sideMenuImage = sideMenuImageRepository.findByImgId(id).orElseThrow();
        String fileName = sideMenuImage.getImgPath();

        return sideMenuPath + "/" + fileName;
    }

}
