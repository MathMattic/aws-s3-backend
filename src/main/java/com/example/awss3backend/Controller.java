package com.example.awss3backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {

    @Value("${BUCKET}")
    private String bucket;


    @GetMapping("/")
    public void get() {


//        ProfileCredentialsProvider cp = ProfileCredentialsProvider.builder().profileName("default").build();
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;

        S3Client s3 = S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build();

        ListObjectsV2Request listObjects = ListObjectsV2Request
                .builder()
                .bucket(bucket)
                .build();

//        s3.listObjects(listObjects).contents().stream().forEach(x -> System.out.println(x.key()));

        ListObjectsV2Response res = s3.listObjectsV2(listObjects);
        List<S3Object> objects = res.contents();

//        URL url = s3.utilities().getUrl(GetUrlRequest.builder().bucket(bucket).key().build());


        for (S3Object myValue : objects) {
            System.out.println(myValue.key() + " " + myValue.size() + " " + myValue.lastModified());
        }

        // GetContentType
        HeadObjectRequest headObjectRequest  = HeadObjectRequest.builder()
                .bucket(bucket)
                .key("${KEY}")
                .build();
        HeadObjectResponse headObjectResponse = s3.headObject(headObjectRequest);

        System.out.println(headObjectResponse.contentType());

    }

}
