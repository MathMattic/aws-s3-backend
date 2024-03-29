package com.example.awss3backend.services;

import com.example.awss3backend.entities.BucketObject;
import com.example.awss3backend.repositories.S3ObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static software.amazon.awssdk.core.sync.RequestBody.fromInputStream;

@Service
public class S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    @Value("${BUCKET}") // local run configuration value // TODO: DB map value
    private String bucket;

    S3ObjectRepository s3ObjectRepository;
    S3Client s3Client;

    public S3Service(S3ObjectRepository s3ObjectRepository) {
        this.s3ObjectRepository = s3ObjectRepository;
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create("default"); // local AWS credentials
        this.s3Client = S3Client.builder().credentialsProvider(credentialsProvider).region(Region.US_EAST_1).build();
    }

    public List<BucketObject> listObjects() {
        System.out.println(s3Client.listBuckets()); // TODO
        ListObjectsV2Request listObjects = ListObjectsV2Request.builder().bucket(bucket).build();
        ListObjectsV2Response response = s3Client.listObjectsV2(listObjects);
        List<S3Object> s3objects = response.contents();

        List<BucketObject> bucketObjects = new ArrayList<>();
        for (S3Object objectInfo : s3objects) {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder().bucket(bucket).key(objectInfo.key()).build();
            HeadObjectResponse headObjRes = s3Client.headObject(headObjectRequest);
            bucketObjects.add(new BucketObject(objectInfo.key(), objectInfo.size(), objectInfo.lastModified().toString(), headObjRes.contentType(), objectInfo.eTag()));
        }
        s3ObjectRepository.saveAll(bucketObjects);
        return bucketObjects;
    }

    public byte[] getObject(String key) {
        GetObjectRequest objectRequest = GetObjectRequest.builder().key(key).bucket(bucket).build();
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
        return objectBytes.asByteArray();
    }

    public BucketObject putObject(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        PutObjectResponse response; // TODO: DB map response. returns eTag and ServerSideEncryption
        try (InputStream is = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(fileName).contentType(file.getContentType()).build();
            response = s3Client.putObject(putObjectRequest, fromInputStream(is, file.getSize()));
            logger.info("PutObjectResponse was: {}", response);
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder().bucket(bucket).key(fileName).build();
            HeadObjectResponse headObjRes = s3Client.headObject(headObjectRequest);
            return s3ObjectRepository.save(new BucketObject(fileName, headObjRes.contentLength(), headObjRes.lastModified().toString(), file.getContentType(), headObjRes.eTag()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String deleteObject(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucket).key(key).build();
        s3Client.deleteObject(deleteObjectRequest).deleteMarker();
        s3ObjectRepository.deleteById(key);
        return key + " deleted.";
    }

    public List<BucketObject> find() {
//        Long x = s3ObjectRepository.deleteByKey("test.txt");
        return s3ObjectRepository.findByContentTypeAndSizeGreaterThan("application/octet-stream", 20L);
    }

} // end class S3Service


