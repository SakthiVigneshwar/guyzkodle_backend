package com.cluegame.funnygame.controller;

import com.cluegame.funnygame.entity.Participant;
import com.cluegame.funnygame.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/participant")
@CrossOrigin(origins = {
        "https://guyzzkodle-frontend.vercel.app",
        "https://guyzzkodle-frontend-six.vercel.app"
})
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    // ✅ Check if participant name exists
    @GetMapping("/check/{name}")
    public boolean checkParticipant(@PathVariable String name) {
        return participantService.isValidParticipant(name);
    }

    // ✅ Record user completion (with slot normalization)
    @PostMapping("/submit")
    public String submitResult(@RequestBody Map<String, Object> body) {
        String name = body.get("name").toString();
        int seconds = Integer.parseInt(body.get("seconds").toString());
        String slot = body.get("slot").toString();

        // 🔥 Normalize slot value (AM → morning, PM → afternoon)
        if (slot.equalsIgnoreCase("AM")) {
            slot = "morning";
        } else if (slot.equalsIgnoreCase("PM")) {
            slot = "afternoon";
        }

        participantService.recordSuccess(name, seconds, slot);
        return "✅ Success";
    }

    // ✅ Get today's leaderboard (all slots)
    @GetMapping("/today")
    public List<Participant> getTodayResults() {
        return participantService.getAllSortedByTimeForToday();
    }

    // ✅ Get results for a specific date (all slots)
    @GetMapping("/date/{date}")
    public List<Participant> getResultsForDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return participantService.getAllSortedByTimeForDate(localDate);
    }

    // ✅ Get results for a specific date + slot
    @GetMapping("/date/{date}/slot/{slot}")
    public List<Participant> getResultsForDateAndSlot(@PathVariable String date, @PathVariable String slot) {
        LocalDate localDate = LocalDate.parse(date);

        // 🔥 Normalize slot again for leaderboard fetch
        if (slot.equalsIgnoreCase("AM")) {
            slot = "morning";
        } else if (slot.equalsIgnoreCase("PM")) {
            slot = "afternoon";
        }

        return participantService.getAllSortedByTimeForDateAndSlot(localDate, slot);
    }

    // ✅ Get all participants (admin only)
    @GetMapping("/all")
    public List<Participant> getAllParticipants() {
        return participantService.getAllParticipants();
    }
}
