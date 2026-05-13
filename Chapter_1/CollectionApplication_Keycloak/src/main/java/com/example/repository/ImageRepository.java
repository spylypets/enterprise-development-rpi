package com.example.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.example.model.Image;

public interface ImageRepository extends R2dbcRepository<Image, Long> {

}
