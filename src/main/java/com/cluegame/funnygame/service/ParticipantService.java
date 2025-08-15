package com.cluegame.funnygame.service;

import com.cluegame.funnygame.entity.Participant;
import com.cluegame.funnygame.repository.ParticipantRepository;
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

    // ✅ Check if a participant name exists
    public boolean isValidParticipant(String name) {
        return participantRepository.findByName(name).isPresent();
    }

    // ✅ Record success attempt (used when UI submits time)
    public void recordSuccess(String name, int seconds) {
        Optional<Participant> optional = participantRepository.findByName(name);
        if (optional.isPresent()) {
            Participant participant = optional.get();
            // Update only on submit
            participant.setAttempts(
                    participant.getAttempts() == null ? 1 : participant.getAttempts() + 1
            );
            participant.setSeconds(seconds);
            participant.setCompletedDate(LocalDate.now());
            participant.setCompletedTime(LocalTime.now());

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
    public List<Participant> getAllSortedByTimeForDateAndSlot(LocalDate date, String slot) {
        return participantRepository.findAllByCompletedDate(date)
                .stream()
                .filter(p -> p.getSeconds() != null && p.getSeconds() > 0)
                .filter(p -> {
                    if (p.getCompletedTime() == null) return false;
                    int hour = p.getCompletedTime().getHour();
                    return "AM".equalsIgnoreCase(slot) ? hour < 12 : hour >= 12;
                })
                .sorted(Comparator.comparingInt(Participant::getSeconds))
                .collect(Collectors.toList());
    }
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }



}
