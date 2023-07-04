package com.example.awss3backend.repositories;

import com.example.awss3backend.BucketObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3ObjectRepository extends JpaRepository <BucketObject, Long> {

}
