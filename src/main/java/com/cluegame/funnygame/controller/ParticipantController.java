package com.cluegame.funnygame.controller;

import com.cluegame.funnygame.entity.Participant;
import com.cluegame.funnygame.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("participant")
@CrossOrigin(origins = "https://guyzzkodle-frontend.vercel.app")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;
    @GetMapping("/check/{name}")
    public boolean checkParticipant(@PathVariable String name) {
        return participantService.isValidParticipant(name);
    }
    @PostMapping("/submit")
    public String submitResult(@RequestBody Map<String, Object> body) {
        String name = body.get("name").toString();
        int seconds = Integer.parseInt(body.get("seconds").toString());
        participantService.recordSuccess(name, seconds);
        return "Success";
    }
    // 🔥 Today's leaderboard
    @GetMapping("/today")
    public List<Participant> getTodayResults() {
        return participantService.getAllSortedByTimeForToday();
    }
    // 🔥 Optional: pass any date (yyyy-mm-dd) to get results
    @GetMapping("/date/{date}")
    public List<Participant> getResultsForDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date); // expects format: yyyy-MM-dd
        return participantService.getAllSortedByTimeForDate(localDate);
    }
    @GetMapping("/all")
    public List<Participant> getAllParticipants() {
        return participantService.getAllParticipants();
    }
    @GetMapping("/date/{date}/slot/{slot}")
    public List<Participant> getResultsForDateAndSlot(@PathVariable String date, @PathVariable String slot) {
        LocalDate localDate = LocalDate.parse(date);
        return participantService.getAllSortedByTimeForDateAndSlot(localDate, slot);
    }

}
