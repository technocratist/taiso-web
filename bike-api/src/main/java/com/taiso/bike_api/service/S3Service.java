package com.taiso.bike_api.service;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

public class S3Service {

    //S3버킷 파일관리 관련 클래스
    private S3Client s3Client;
    private String bucket;

    // 파일 저장 + 파일 Id 생성
    public String uploadFile(MultipartFile file, Long userId) {

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            } else {
                //null 일때 오류 처리필요
            }

            // S3에 저장될 파일 값
            String fileName = "users/" + userId + "/" + UUID.randomUUID() + extension;

            // S3 업로드
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket("1") // 버킷 이름
                    .key(fileName)  // 파일 이름 (S3에서의 객체 키)
                    .contentType(file.getContentType())
                    .build(), software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            return fileName; // S3에 저장된 파일 키 값 반환

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }


    public void close() {
        s3Client.close();
    }

//    public String getFileUrl(String fileKey) {
//        return "https://" + bucket + ".s3.amazonaws.com/" + fileKey;
//    }
}
