package com.cluegame.funnygame.service;

import com.cluegame.funnygame.dto.SubmitResultResponse;
import com.cluegame.funnygame.entity.Participant;
import com.cluegame.funnygame.repository.ParticipantRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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

    public SubmitResultResponse recordSuccess(String name, int seconds, String status, LocalTime attemptTime) {
        Participant participant = participantRepository.findByName(name)
                .orElseGet(() -> {
                    Participant newParticipant = new Participant();
                    newParticipant.setName(name);
                    newParticipant.setAttempts(0);
                    newParticipant.setAttemptDetails(new java.util.ArrayList<>());
                    return newParticipant;
                });

        int currentAttempt = participant.getAttempts() == null ? 0 : participant.getAttempts();

        // Get previous attempt result
        String previousAttemptResult = null;
        List<Participant.Attempt> attemptsList = participant.getAttemptDetails();
        if (!attemptsList.isEmpty()) {
            previousAttemptResult = attemptsList.get(attemptsList.size() - 1).getStatus();
        }

        // Increment current attempt count
        currentAttempt++;

        participant.setAttempts(currentAttempt);
        participant.setSeconds(seconds);
        participant.setCompletedDate(LocalDate.now());

        Participant.Attempt newAttempt = new Participant.Attempt(
                LocalDate.now(),
                attemptTime,
                currentAttempt,
                status,
                seconds
        );

        participant.getAttemptDetails().add(newAttempt);

        participantRepository.save(participant);

        return new SubmitResultResponse(
                participant.getAttemptDetails().size(),
                currentAttempt,
                previousAttemptResult
        );
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
}
