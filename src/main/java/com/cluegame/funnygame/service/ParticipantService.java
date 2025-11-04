package com.cluegame.funnygame.service;

import com.cluegame.funnygame.entity.Participant;
import com.cluegame.funnygame.repository.ParticipantRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @PostConstruct
    public void preloadParticipants() {
        participantRepository.deleteAll();

        List<String> names = List.of(
                "kandy",
                "sreethorfinn",
                "aadhuii",
                "harikrishh",
                "aadhispeed",
                "achchu",
                "suruthi",
                "logaprakash",
                "srenithii",
                "briny",
                "sakthii",
                "brindss"
        );

        List<Participant> participants = names.stream().map(name -> {
            Participant p = new Participant();
            p.setName(name);
            p.setAttempts(null);
            p.setSeconds(null);
            p.setCompletedDate(null);
            p.setCompletedTime(null);
            p.setSlot(null); // ✅ Added slot for new logic
            return p;
        }).toList();

        participantRepository.saveAll(participants);
        System.out.println("✅ Preloaded participant names into MongoDB.");
    }

    // ✅ Check if a participant name exists
    public boolean isValidParticipant(String name) {
        return participantRepository.findByName(name).isPresent();
    }

    // ✅ Record success attempt (per date + slot)
    public void recordSuccess(String name, int seconds, String slot) {
        LocalDate today = LocalDate.now();
        ZoneId istZone = ZoneId.of("Asia/Kolkata");

        // Normalize slot (in case frontend sends AM/PM or morning/afternoon)
        String normalizedSlot = normalizeSlot(slot);

        // Check for existing record (same name + date + slot)
        Optional<Participant> optional =
                participantRepository.findByNameAndCompletedDateAndSlot(name, today, normalizedSlot);

        Participant participant;
        if (optional.isPresent()) {
            participant = optional.get();
            participant.setAttempts(
                    participant.getAttempts() == null ? 1 : participant.getAttempts() + 1
            );
        } else {
            participant = new Participant();
            participant.setName(name);
            participant.setAttempts(1);
            participant.setCompletedDate(today);
            participant.setSlot(normalizedSlot); // ✅ morning/afternoon
        }

        participant.setSeconds(seconds);
        participant.setCompletedTime(LocalTime.now(istZone));
        participantRepository.save(participant);
    }

    // ✅ Normalize slot values (AM → morning, PM → afternoon)
    private String normalizeSlot(String slot) {
        if (slot == null) return null;
        slot = slot.trim().toLowerCase();
        if (slot.equals("am") || slot.equals("morning")) return "morning";
        if (slot.equals("pm") || slot.equals("afternoon")) return "afternoon";
        return slot;
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

    // ✅ Get participants for specific date & slot
    public List<Participant> getAllSortedByTimeForDateAndSlot(LocalDate date, String slot) {
        String normalizedSlot = normalizeSlot(slot);

        return participantRepository.findAllByCompletedDate(date)
                .stream()
                .filter(p -> p.getSeconds() != null && p.getSeconds() > 0)
                .filter(p -> p.getSlot() != null && p.getSlot().equalsIgnoreCase(normalizedSlot))
                .sorted(Comparator.comparingInt(Participant::getSeconds))
                .collect(Collectors.toList());
    }

    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }
}
