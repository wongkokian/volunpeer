package com.project.volunpeer.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.volunpeer.db.entity.PeerEntity;

@Repository
public interface PeerRepository extends MongoRepository<PeerEntity, PeerEntity.Key> {
}