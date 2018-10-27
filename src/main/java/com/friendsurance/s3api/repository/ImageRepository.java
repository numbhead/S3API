package com.friendsurance.s3api.repository;

import org.springframework.data.repository.CrudRepository;

import com.friendsurance.s3api.model.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {

}
