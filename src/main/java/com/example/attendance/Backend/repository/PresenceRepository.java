package com.example.attendance.Backend.repository;

import com.example.attendance.Backend.entity.Presence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PresenceRepository extends MongoRepository<Presence, String> {
}
