package com.cluegame.funnygame.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "clue_sets")
public class ClueSet {
    @Id
    private String id;

    private String date; // yyyy-MM-dd
    private String slot; // morning / evening

    private List<String> clues;
    private String answer;

    public ClueSet() {}

    public ClueSet(String date, String slot, List<String> clues, String answer) {
        this.date = date;
        this.slot = slot;
        this.clues = clues;
        this.answer = answer;
    }

    // 🔧 Getters and Setters

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getSlot() {
        return slot;
    }

    public List<String> getClues() {
        return clues;
    }

    public String getAnswer() {
        return answer;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public void setClues(List<String> clues) {
        this.clues = clues;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
