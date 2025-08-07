package com.cluegame.funnygame.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("cluesets")
public class ClueSet {
    @Id
    private String id;

    private String date; // yyyy-MM-dd
    private String slot; // "morning" or "evening"

    private List<String> clues;
    private String answer;
}
