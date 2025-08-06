package com.cluegame.funnygame.repository;

import com.cluegame.funnygame.entity.Participant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends MongoRepository<Participant, String> {
    Optional<Participant> findByName(String name);
    List<Participant> findAllByCompletedDate(LocalDate date);
}
