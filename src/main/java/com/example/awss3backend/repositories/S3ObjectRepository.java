package com.example.awss3backend.repositories;

import com.example.awss3backend.entities.BucketObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface S3ObjectRepository extends JpaRepository <BucketObject, String> {

    List<BucketObject> findByKey(String name);
    void deleteByKey(String key); // use @Transactional where this is called (custom query)

//    @Query("SELECT u FROM BucketObject u WHERE u.contentType = :contentType AND u.size >= :size")
    List<BucketObject> findByContentTypeAndSizeGreaterThan(@Param("contentType") String contentType, @Param("size") Long size);

}
