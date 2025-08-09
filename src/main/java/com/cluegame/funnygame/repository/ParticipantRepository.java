package com.cluegame.funnygame.repository;

import com.cluegame.funnygame.entity.Participant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends MongoRepository<Participant, String> {

    Optional<Participant> findByName(String name);

    Optional<Participant> findByNameAndCompletedDate(String name, LocalDate completedDate);

    List<Participant> findAllByCompletedDate(LocalDate date);

    List<Participant> findAllByCompletedDateAndStatus(LocalDate date, String status);

    List<Participant> findAllByCompletedDateAndStatusAndAttemptNumber(
            LocalDate date, String status, Integer attemptNumber);

    List<Participant> findAllByAttemptDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
