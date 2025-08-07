package com.cluegame.funnygame.repository;

import com.cluegame.funnygame.entity.ClueSet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClueSetRepository extends MongoRepository<ClueSet, String> {
    Optional<ClueSet> findByDateAndSlot(String date, String slot);
}
