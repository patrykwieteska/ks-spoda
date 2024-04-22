package pl.spoda.ks.euro.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupStageTeamStatus {

    WINNER(1),
    RUNNER_UP(2),
    THIRD_PLACE(3);

    private final int position;
}
