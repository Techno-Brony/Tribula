package io.github.techno_brony.tribula.internals.interfaces;

import io.github.techno_brony.tribula.internals.enums.TribulaPlayerDifficulty;

public interface ITribulaPlayer {
    TribulaPlayerDifficulty getStateDifficulty();
    int getStateLevel();
    void getStateTribe(); //TODO Replace with class or enum
    void getStateClass(); //TODO Replace with class or enum

    //TODO To be completed
}
