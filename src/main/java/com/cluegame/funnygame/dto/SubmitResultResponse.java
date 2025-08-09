package com.cluegame.funnygame.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitResultResponse {
    private int totalAttempts;
    private int currentAttempt;
    private String previousAttemptResult;  // Win or Lose
}
