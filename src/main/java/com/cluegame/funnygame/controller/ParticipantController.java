package com.cluegame.funnygame.controller;

import com.cluegame.funnygame.entity.Participant;
import com.cluegame.funnygame.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/participant")
@CrossOrigin(origins = "http://localhost:3000")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    @GetMapping("/check/{name}")
    public boolean checkParticipant(@PathVariable String name) {
        return participantService.isValidParticipant(name);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitResult(@RequestBody Map<String, Object> body) {
        try {
            String name = body.get("name").toString();
            int seconds = Integer.parseInt(body.get("seconds").toString());
            String status = body.get("status").toString(); // WIN / LOSS
            int attemptNumber = Integer.parseInt(body.get("attemptNumber").toString());

            participantService.recordResult(name, seconds, status, attemptNumber);

            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/today")
    public List<Participant> getTodayResults() {
        return participantService.getAllSortedByTimeForToday();
    }

    @GetMapping("/date/{date}")
    public List<Participant> getResultsForDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return participantService.getAllSortedByTimeForDate(localDate);
    }

    @GetMapping("/all")
    public List<Participant> getAllParticipants() {
        return participantService.getAllParticipants();
    }
}
