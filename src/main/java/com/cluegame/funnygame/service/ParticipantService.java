package com.cluegame.funnygame.service;

import com.cluegame.funnygame.entity.Participant;
import com.cluegame.funnygame.repository.ParticipantRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;


    // ✅ Only preload names – leave other fields unset (MongoDB won't store them)
    @PostConstruct
    public void preloadNames() {
        participantRepository.deleteAll();

        List<String> names = List.of(
                "brindhaMK", "sreekandhaMK", "sreethorfinn", "aadhuii",
                "harikrishh", "aadhiakimichi", "achchu", "surthi",
                "logaprasath", "srenithi"
        );

        for (String name : names) {
            participantRepository.findByName(name).orElseGet(() -> {
                Participant p = new Participant();
                p.setName(name);
                return participantRepository.save(p);
            });
        }
    }

    // ✅ Check if a participant name exists
    public boolean isValidParticipant(String name) {
        return participantRepository.findByName(name).isPresent();
    }

    // ✅ Record success attempt (used when UI submits time)
    public void recordSuccess(String name, int seconds) {
        Optional<Participant> optional = participantRepository.findByName(name);
        if (optional.isPresent()) {
            Participant participant = optional.get();

            participant.setAttempts(
                    participant.getAttempts() == null ? 1 : participant.getAttempts() + 1
            );
            participant.setSeconds(seconds);
            participant.setCompletedDate(LocalDate.now());
            participantRepository.save(participant);
        }
    }

    // ✅ Get today's participants with time, sorted by seconds
    public List<Participant> getAllSortedByTimeForToday() {
        LocalDate today = LocalDate.now();
        return participantRepository.findAllByCompletedDate(today)
                .stream()
                .filter(p -> p.getSeconds() != null && p.getSeconds() > 0)
                .sorted(Comparator.comparingInt(Participant::getSeconds))
                .collect(Collectors.toList());
    }

    // ✅ Get participants for any date
    public List<Participant> getAllSortedByTimeForDate(LocalDate date) {
        return participantRepository.findAllByCompletedDate(date)
                .stream()
                .filter(p -> p.getSeconds() != null && p.getSeconds() > 0)
                .sorted(Comparator.comparingInt(Participant::getSeconds))
                .collect(Collectors.toList());
    }

    // ✅ Get all participants (useful for debugging or admin UI)
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }
    // ✅ Record success or loss attempt
    public void recordSuccess(String name, int seconds, String status) {
        // Count how many attempts already made today
        LocalDate today = LocalDate.now();
        List<Participant> todayAttempts = participantRepository.findAll()
                .stream()
                .filter(p -> p.getName().equals(name) && p.getAttemptDateTime() != null &&
                        p.getAttemptDateTime().toLocalDate().equals(today))
                .collect(Collectors.toList());

        int nextAttempt = todayAttempts.size() + 1;

        Participant attemptRecord = new Participant();
        attemptRecord.setName(name);
        attemptRecord.setAttemptNumber(nextAttempt);
        attemptRecord.setSeconds(seconds);
        attemptRecord.setAttemptDateTime(java.time.LocalDateTime.now());
        attemptRecord.setStatus(status.toUpperCase()); // WIN or LOSS

        participantRepository.save(attemptRecord);
    }

}
