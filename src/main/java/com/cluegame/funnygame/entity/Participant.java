package com.cluegame.funnygame.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "participant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    @Id
    private String id; // MongoDB uses String IDs by default

    private String name;
    private Integer attemptNumber   ; // attempt count per player per day
    private Integer seconds;
    private LocalDateTime attemptDateTime; // exact date+time of attempt
    private String status; // "WIN" or "LOSS"
    private Integer attempts;
    private LocalDate completedDate;

}
