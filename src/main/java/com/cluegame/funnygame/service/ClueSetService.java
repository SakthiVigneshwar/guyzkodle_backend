package com.cluegame.funnygame.service;

import com.cluegame.funnygame.entity.ClueSet;
import com.cluegame.funnygame.repository.ClueSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClueSetService {

    @Autowired
    private ClueSetRepository repository;

    public Optional<ClueSet> getClueSetByDateAndSlot(String date, String slot) {
        return repository.findByDateAndSlot(date, slot);
    }

    public ClueSet saveClueSet(ClueSet clueSet) {
        // overwrite if existing
        Optional<ClueSet> existing = repository.findByDateAndSlot(clueSet.getDate(), clueSet.getSlot());
        existing.ifPresent(existingClue -> clueSet.setId(existingClue.getId()));
        return repository.save(clueSet);
    }
}
