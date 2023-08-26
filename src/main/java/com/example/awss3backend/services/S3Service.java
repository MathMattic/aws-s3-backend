package com.example.awss3backend.services;


import com.example.awss3backend.entities.BucketObject;
import com.example.awss3backend.repositories.S3ObjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

@Service
public class S3Service {

    @Value("${BUCKET}")
    private String bucket;

    @Value("${KEY}")
    private String key;

    S3ObjectRepository s3ObjectRepository;
    S3Client s3Client;

    public S3Service(S3ObjectRepository s3ObjectRepository) {
        this.s3ObjectRepository = s3ObjectRepository;
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create("default");
        this.s3Client = S3Client.builder().credentialsProvider(credentialsProvider).region(Region.US_EAST_1).build();
    }


    // List the objects in the bucket
    public List<BucketObject> ListObjects() {

        ListObjectsV2Request listObjects = ListObjectsV2Request.builder().bucket(bucket).build();

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



    public byte[] getObject(String keyz) {

        GetObjectRequest objectRequest = GetObjectRequest.builder().key(keyz).bucket(bucket).build();
        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
        return objectBytes.asByteArray();

    }

    public String putObject(MultipartFile file) {

        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder().bucket(bucket).key(file.getOriginalFilename());
        PutObjectRequest putObjectRequest = requestBuilder.build();
        PutObjectResponse response = s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(is, file.getSize()));

        if (response.sdkHttpResponse().isSuccessful()) {
            return "File uploaded successfully";
        } else {
            return "Upload failed: " + response.sdkHttpResponse().statusText().orElse("Unknown error");
        }

    }


    public String deleteObject() {

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucket).key(key).build();

        DeleteObjectResponse deleteObjectResponse = s3Client.deleteObject(deleteObjectRequest);

        if (deleteObjectResponse.sdkHttpResponse().isSuccessful()) {
            return "Object deleted successfully";
        } else {
            return "Delete failed: " + deleteObjectResponse.sdkHttpResponse().statusText().orElse("Unknown error");
        }
    }


} // end class S3Service


