package com.example.awss3backend;

import com.example.awss3backend.repositories.ObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {

    @Value("${BUCKET}")
    private String bucket;

    @Autowired
    private ObjectRepository ObjectRepository;


    @GetMapping("/")
    public ResponseEntity<List<BucketObject>> listBucketObjects() {

        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.builder().profileName("default").build();
//        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();

        S3Client s3 = S3Client.builder().credentialsProvider(credentialsProvider).region(Region.US_EAST_1).build();

        ListObjectsV2Request listObjects = ListObjectsV2Request
                .builder()
                .bucket(bucket)
                .build();

        ListObjectsV2Response res = s3.listObjectsV2(listObjects);
        List<S3Object> s3objects = res.contents();

        List<BucketObject> bucketObjects = new ArrayList<>();
        for (S3Object objectInfo : s3objects) {

            HeadObjectRequest headObjectRequest  = HeadObjectRequest.builder().bucket(bucket).key(objectInfo.key()).build();
            HeadObjectResponse headObjectResponse = s3.headObject(headObjectRequest);

            bucketObjects.add(new BucketObject(objectInfo.key(), objectInfo.size(), objectInfo.lastModified().toString(), headObjectResponse.contentType()));

            ObjectRepository.saveAll(bucketObjects);
        }


        return ResponseEntity.ok(bucketObjects);
    }

}
