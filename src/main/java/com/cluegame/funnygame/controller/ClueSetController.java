package com.cluegame.funnygame.controller;

import com.cluegame.funnygame.entity.ClueSet;
import com.cluegame.funnygame.service.ClueSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/clues")
@CrossOrigin(origins = {
        "https://guyzzkodle-frontend.vercel.app",
        "https://guyzzkodle-frontend-six.vercel.app"
})
public class ClueSetController {

    @Autowired
    private ClueSetService service;

    // ✅ For user frontend - combine both morning & afternoon slots
    @GetMapping
    public Map<String, Object> getClueSets(@RequestParam String date) {
        Map<String, Object> response = new HashMap<>();

        Optional<ClueSet> morningOpt = service.getClueSetByDateAndSlot(date, "morning");
        Optional<ClueSet> afternoonOpt = service.getClueSetByDateAndSlot(date, "afternoon");

        // Prepare morning data
        Map<String, Object> morningData = new HashMap<>();
        if (morningOpt.isPresent()) {
            ClueSet morning = morningOpt.get();
            morningData.put("clues", morning.getClues());
            morningData.put("answer", morning.getAnswer());
        } else {
            morningData.put("clues", List.of());
            morningData.put("answer", "");
        }

        // Prepare afternoon data
        Map<String, Object> afternoonData = new HashMap<>();
        if (afternoonOpt.isPresent()) {
            ClueSet afternoon = afternoonOpt.get();
            afternoonData.put("clues", afternoon.getClues());
            afternoonData.put("answer", afternoon.getAnswer());
        } else {
            afternoonData.put("clues", List.of());
            afternoonData.put("answer", "");
        }

        response.put("morning", morningData);
        response.put("afternoon", afternoonData);

        return response;
    }

    // ✅ For admin - save both morning & afternoon together
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
                morningSet.setClues((List<String>) morning.get("clues"));
                morningSet.setAnswer((String) morning.get("answer"));
                service.saveClueSet(morningSet);
            }

            if (afternoon != null) {
                ClueSet afternoonSet = new ClueSet();
                afternoonSet.setDate(date);
                afternoonSet.setSlot("afternoon");
                afternoonSet.setClues((List<String>) afternoon.get("clues"));
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
