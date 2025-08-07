package com.cluegame.funnygame.service;

import com.cluegame.funnygame.entity.ClueSet;
import com.cluegame.funnygame.repository.ClueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class ClueService {

    @Autowired
    private ClueRepository clueRepository;

    public void saveClueSet(ClueSet clueSet) {
        clueRepository.save(clueSet);
    }

    public Optional<ClueSet> getClueSetForCurrentTimeSlot() {
        LocalDate today = LocalDate.now();
        String slot = getCurrentSlot();
        return clueRepository.findByDateAndSlot(today.toString(), slot);
    }

    private String getCurrentSlot() {
        LocalTime now = LocalTime.now();
        return now.isBefore(LocalTime.NOON) ? "morning" : "evening";
    }
}
