package com.ll.ticket.domain.customer.question.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStorageUtil {

    @Value("${custom.file-path}")
    private String filePath; //yml 에 경로 저장 questionDetail.html 에 도따로 경로 지정해줘야함

    public String saveFile(MultipartFile multipartFile) {


        try {
                //파일이 null 이 아니 거나 파일이 없지 않은 경우 if 문 실행
            if (multipartFile != null && !multipartFile.isEmpty()) { //파일을 업로드 하지 않았을경우 에 빈문자열 저장

                File directory = new File(filePath);

            if (!directory.exists()) { //파일 저장경로에 디렉토리 가 없으면 생성
                directory.mkdir();
            }
            UUID uuid = UUID.randomUUID(); // 파일 uuid 생성

            String fileName = uuid + "_" + multipartFile.getOriginalFilename(); // uuid  + 파일이름

            File saveFile = new File(filePath, fileName);

            multipartFile.transferTo(saveFile);

            return fileName; // uuid  + 파일이름 반환

            } else {
                // 파일이 업로드 되지 않았을 때의 처리
                return ""; // 빈 문자열 반환
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 전송 중 오류가 발생했습니다.", e);
        }
    }


    //파일이 존재 하면 삭제
    public void deleteFile(String fileName) {
        if (fileName != null) {
            File oldFile = new File(filePath, fileName);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }
    }
}