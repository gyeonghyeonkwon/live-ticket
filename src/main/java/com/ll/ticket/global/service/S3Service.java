package com.ll.ticket.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.ll.ticket.global.s3.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public File convertMultipartFileToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File convertedFile = File.createTempFile("temp", fileName);
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convertedFile;
    }

//    @Transactional
//    public String uploadFile(MultipartFile multipartFile) throws IOException {
//        AmazonS3 s3 = s3Config.amazonS3Client();
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentType(multipartFile.getContentType());
//        objectMetadata.setContentLength(multipartFile.getSize());
//
//        String originalFilename = multipartFile.getOriginalFilename();
//        int index = originalFilename.lastIndexOf(".");
//        String ext = originalFilename.substring(index + 1);
//
//        String storeFileName = UUID.randomUUID() + "." + ext;
//
//        String key = "test/" + storeFileName; //TODO test 대신에 사용할 것 찾기
//
//        InputStream inputStream = multipartFile.getInputStream();
//
//        s3.putObject(bucket, key, inputStream);
//
////        try (InputStream inputStream = multipartFile.getInputStream()) {
////            s3.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
////                    .withCannedAcl(CannedAccessControlList.PublicRead));
////        }
//
//        return s3.getUrl(bucket, key).toString();
//    }

    public String createImage(MultipartFile multipartFile) {
        String uploadFileDTO = uploadImageToS3(multipartFile);

        // 이미지 업로드에 대한 처리 (여기서는 간단한 예시로 URL 로깅만)

        // 추가적으로 필요한 로직 수행

        return uploadFileDTO;
    }

    public String uploadImageToS3(MultipartFile imageFile) {
        AmazonS3 s3 = s3Config.amazonS3Client();

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + imageFile.getOriginalFilename();

        // For temporary storage to upload
        File uploadImageFile = null;

        try {
            uploadImageFile = convertMultipartFileToFile(imageFile, imageFileName);

            String imageObjectPath = "images/" + imageFileName;

            s3.putObject(bucket, imageObjectPath, uploadImageFile);

            String baseUploadURL = "https://kr.object.ncloudstorage.com/" + bucket + "/";
            String imageURL = baseUploadURL + imageObjectPath;

            // direct access URL
//            log.info("Image URL: {}", imageURL);

//            System.out.println("imageURL = " + imageURL);

            // All Users can access the object
            setAcl(s3, imageObjectPath);

            return imageURL;

        } catch (AmazonS3Exception e) { // ACL Exception
//            log.info(e.getErrorMessage());
            System.exit(1);
            return null; // if error during upload, return null
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Delete temporary file used when uploading
            assert uploadImageFile != null;

            uploadImageFile.delete();
        }
    }

    public void setAcl(AmazonS3 s3, String objectPath) {
        AccessControlList objectAcl = s3.getObjectAcl(bucket, objectPath);
        objectAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        s3.setObjectAcl(bucket, objectPath, objectAcl);
    }
}