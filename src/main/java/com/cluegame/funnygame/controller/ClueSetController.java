package com.cluegame.funnygame.controller;

import com.cluegame.funnygame.entity.ClueSet;
import com.cluegame.funnygame.service.ClueSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/clues")
@CrossOrigin(origins = "https://guyzzkodle-frontend.vercel.app") // ✅ safer CORS
public class ClueSetController {

    @Autowired
    private ClueSetService service;

    // ✅ Combine both morning & afternoon in a single response
    @GetMapping
    public Map<String, Object> getClueSets(@RequestParam String date) {
        Map<String, Object> response = new HashMap<>();

        Optional<ClueSet> morning = service.getClueSetByDateAndSlot(date, "morning");
        Optional<ClueSet> afternoon = service.getClueSetByDateAndSlot(date, "afternoon");

        response.put("morning", morning.orElse(null));
        response.put("afternoon", afternoon.orElse(null));

        return response;
    }

    // ✅ Save morning & afternoon together
    @PostMapping("/save")
    public String saveClueSet(@RequestBody Map<String, Object> body) {
        try {
            String date = (String) body.get("date");

            Map<String, Object> morning = (Map<String, Object>) body.get("morning");
            Map<String, Object> afternoon = (Map<String, Object>) body.get("afternoon");

            if (morning != null) {
                ClueSet morningSet = new ClueSet();
                morningSet.setDate(date);
                morningSet.setSlot("morning");
                morningSet.setClues((java.util.List<String>) morning.get("clues"));
                morningSet.setAnswer((String) morning.get("answer"));
                service.saveClueSet(morningSet);
            }

            if (afternoon != null) {
                ClueSet afternoonSet = new ClueSet();
                afternoonSet.setDate(date);
                afternoonSet.setSlot("afternoon");
                afternoonSet.setClues((java.util.List<String>) afternoon.get("clues"));
                afternoonSet.setAnswer((String) afternoon.get("answer"));
                service.saveClueSet(afternoonSet);
            }

            return "✅ Clues saved successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to save clues!";
        }
    }
}
