package com.example.awss3backend.services;


import com.example.awss3backend.BucketObject;
import com.example.awss3backend.repositories.S3ObjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service {

    // TODO: Choose your bucket name
    @Value("${BUCKET}")
    private String bucket;

    S3ObjectRepository s3ObjectRepository;
    S3Client s3Client;

    public S3Service(S3ObjectRepository s3ObjectRepository) {
        this.s3ObjectRepository = s3ObjectRepository;
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create("default");
        this.s3Client = S3Client.builder().credentialsProvider(credentialsProvider).region(Region.US_EAST_1).build();
    }


    // List the objects in the bucket
    public List<BucketObject> ListObjects() {

        ListObjectsV2Request listObjects = ListObjectsV2Request
                .builder()
                .bucket(bucket)
                .build();

        ListObjectsV2Response res = s3Client.listObjectsV2(listObjects);
        List<S3Object> s3objects = res.contents();

        List<BucketObject> bucketObjects = new ArrayList<>();
        for (S3Object objectInfo : s3objects) {

            HeadObjectRequest headObjectRequest  = HeadObjectRequest.builder().bucket(bucket).key(objectInfo.key()).build();
            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);

            bucketObjects.add(new BucketObject(objectInfo.key(), objectInfo.size(), objectInfo.lastModified().toString(), headObjectResponse.contentType()));

        }
        s3ObjectRepository.saveAll(bucketObjects);

        return bucketObjects;

    } // end ListObjects()



} // end class S3Service


