package com.example.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.model.CollectionItem;

@Repository
public interface CollectionItemRepository extends CustomizedCollectionItemRepository, R2dbcRepository<CollectionItem, Long> {}