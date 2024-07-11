package com.osanvalley.moamail.global.config.s3;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AwsS3Service {

    /**
     * 파일 여러 개를 리스트로 가져와 업로드한 파일명 리스트를 반환합니다
     * @param multipartFiles 멀티 파트 파일 헤더 설정한 업로드 파일 리스트
     * @param dirName 업로드 디렉토릴 경로
     * @return fileNameList BaseURL 을 제외한 파일 경로 리스트
     * */
    List<String> uploadImage(List<MultipartFile> multipartFiles, String dirName);

    /**
     * 지정한 파일 명에 해당하는 파일을 S3 버킷에서 제거합니다
     * 서버에 존재하지 않는 파일을 삭제 시도하더라도 예외를 내보내지 않습니다
     * @param fileName 디렉토리 경로를 포함한 파일명
     * */
    void deleteImage(String fileName);

    String uploadImage(MultipartFile multipartFile, String dirName);
}
