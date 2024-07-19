package org.likelion.likelion_12th_team05.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.likelion_12th_team05.common.error.ErrorCode;
import org.likelion.likelion_12th_team05.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    //amazon s3 클라이언트 주입
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // multipartFile -> file 로 전환 후 s3에 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)    // multipartfile -> file 변환하는 메서드 작성
                .orElseThrow( () -> new NotFoundException(ErrorCode.FILE_NOT_FOUND_EXCEPTION,
                        ErrorCode.FILE_NOT_FOUND_EXCEPTION.getMessage()));
        return upload(uploadFile, dirName);
    }

    // file을 s3에 업로드
    public String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName(); // 디렉토리이름/파일이름 <= s3에 저장할 파일 이름 생성
        // 파일을 s3에 업로드하고 url 반환
        String uploadImageUrl = putS3(uploadFile, fileName);

        // 변환 과정(convert)에서 생성된 로컬 파일 삭제
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    public String putS3(File uploadFile, String fileName) {
        // s3 버킷에다가 파일 업로드
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile) // 파일 업로드
        );
        return amazonS3.getUrl(bucket, fileName).toString(); // 업로드 된 파일 url 반환
    }

    // convert 과정에서 생성된 로컬 파일 삭제
    public void removeNewFile(File targetFile) {
        String name = targetFile.getName();

        if (targetFile.delete()) {
            log.info(name + "파일 삭제 완료");
        } else {
            log.info(name + "파일 삭제 실패");
        }
    }

    // multipartfile -> file 로 전환
    public Optional<File> convert(MultipartFile multipartFile) throws IOException {
        // 파일 이름 보관
        // 기존 파일 이름(getoriginalfilename)으로 새로운 File 객체 생성(로컬 디렉토리에 위치)
        File convertFile = new File(multipartFile.getOriginalFilename());
        if (convertFile.createNewFile()) {  // 해당 경로에 파일이 없을 경우, 새 파일 생성
            try(FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());    // multipartFile의 내용을 getbyte 해서 write
            }
            // 해당 파일(convertfile)을 포함한 optional 객체 리턴
            return Optional.of(convertFile);
        }
        // 새 파일 생성을 하지 못 한 경우, 비어있는 Optional 객체를 반환
        return Optional.empty();
    }

}