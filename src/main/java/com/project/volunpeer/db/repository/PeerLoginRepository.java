package com.project.volunpeer.db.repository;

import com.project.volunpeer.db.entity.LoginEntity;
import com.project.volunpeer.db.entity.PeerLoginEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeerLoginRepository extends MongoRepository<PeerLoginEntity, LoginEntity.Key> {
    Optional<PeerLoginEntity> findByUsername(String username);

    Boolean existsByUsername(String username);
}