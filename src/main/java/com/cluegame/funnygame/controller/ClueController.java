package com.cluegame.funnygame.controller;

import com.cluegame.funnygame.entity.ClueSet;
import com.cluegame.funnygame.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/clue")
@CrossOrigin(origins = "*") // Change this for production
public class ClueController {

    @Autowired
    private ClueService clueService;

    @PostMapping("/save")
    public String saveClueSet(@RequestBody ClueSet clueSet) {
        clueService.saveClueSet(clueSet);
        return "Saved";
    }

    @GetMapping("/today")
    public Optional<ClueSet> getCurrentClueSet() {
        return clueService.getClueSetForCurrentTimeSlot();
    }
}
