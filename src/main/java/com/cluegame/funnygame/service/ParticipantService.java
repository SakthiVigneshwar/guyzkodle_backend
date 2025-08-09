package com.cluegame.funnygame.service;

import com.cluegame.funnygame.entity.Participant;
import com.cluegame.funnygame.repository.ParticipantRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    // ✅ Preload only names – leave other fields unset
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
                p.setAttempts(0);
                return participantRepository.save(p);
            });
        }
    }

    // ✅ Check if participant name exists
    public boolean isValidParticipant(String name) {
        return participantRepository.findByName(name).isPresent();
    }

    // ✅ Unified record method for WIN/LOSS
    public void recordResult(String name, int seconds, String status, int attemptNumber) {
        LocalDate today = LocalDate.now();

        Participant participant = participantRepository
                .findByNameAndCompletedDate(name, today)
                .orElseGet(() -> participantRepository.findByName(name).orElse(new Participant()));

        participant.setName(name);
        participant.setAttemptNumber(attemptNumber);
        participant.setSeconds(seconds > 0 ? seconds : null);
        participant.setAttemptDateTime(LocalDateTime.now());
        participant.setStatus(status);

        // Attempts count
        if (participant.getAttempts() == null) {
            participant.setAttempts(1);
        } else {
            participant.setAttempts(participant.getAttempts() + 1);
        }

        // WIN → completedDate = today
        if ("WIN".equalsIgnoreCase(status)) {
            participant.setCompletedDate(today);
        }

        participantRepository.save(participant);
    }

    // ✅ Today's leaderboard
    public List<Participant> getAllSortedByTimeForToday() {
        LocalDate today = LocalDate.now();
        return participantRepository.findAllByCompletedDate(today)
                .stream()
                .filter(p -> p.getSeconds() != null && p.getSeconds() > 0)
                .sorted(Comparator.comparingInt(Participant::getSeconds))
                .collect(Collectors.toList());
    }

    // ✅ Get participants for a given date
    public List<Participant> getAllSortedByTimeForDate(LocalDate date) {
        return participantRepository.findAllByCompletedDate(date)
                .stream()
                .filter(p -> p.getSeconds() != null && p.getSeconds() > 0)
                .sorted(Comparator.comparingInt(Participant::getSeconds))
                .collect(Collectors.toList());
    }

    // ✅ Get all participants (admin/debug)
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }
}
