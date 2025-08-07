package com.cluegame.funnygame.controller;

import com.cluegame.funnygame.entity.ClueSet;
import com.cluegame.funnygame.service.ClueSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/clues")
@CrossOrigin(origins = "*") // 🔥 Add Vercel frontend origin here if needed
public class ClueSetController {

    @Autowired
    private ClueSetService service;

    @GetMapping
    public Optional<ClueSet> getClueSet(@RequestParam String date, @RequestParam String slot) {
        return service.getClueSetByDateAndSlot(date, slot);
    }

    @PostMapping("/save")
    public ClueSet saveClueSet(@RequestBody ClueSet clueSet) {
        return service.saveClueSet(clueSet);
    }
}
