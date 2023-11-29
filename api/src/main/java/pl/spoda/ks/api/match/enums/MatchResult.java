package pl.spoda.ks.api.match.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MatchResult {

    WIN(3),
    LOSE(0),
    DRAW(1);

    private final Integer points;
}
