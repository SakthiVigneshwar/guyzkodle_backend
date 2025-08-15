package com.cluegame.funnygame.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "participant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    @Id
    private String id; // MongoDB uses String IDs by default

    private String name;
    private Integer attempts;
    private Integer seconds;
    private LocalDate completedDate;
    @JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Kolkata")
    private LocalTime completedTime;

}
