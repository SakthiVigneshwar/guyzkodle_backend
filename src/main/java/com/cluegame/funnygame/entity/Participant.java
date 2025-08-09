package com.cluegame.funnygame.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "participant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    @Id
    private String id;

    private String name;

    private Integer attempts;  // current attempt count

    private Integer seconds;   // seconds taken on last attempt

    private LocalDate completedDate;  // last attempt date

    private List<Attempt> attemptDetails = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attempt {
        private LocalDate date;
        private LocalTime time;
        private int attemptNumber;
        private String status;  // Win or Lose
        private int seconds;
    }
}
