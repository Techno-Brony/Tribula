package io.github.techno_coder.tribula.internals.interfaces;

import io.github.techno_coder.tribula.internals.enums.TribulaPlayerDifficulty;

public interface ITribulaPlayer {
    TribulaPlayerDifficulty getStateDifficulty();
    int getStateLevel();
    void getStateTribe(); //TODO Replace with class or enum
    void getStateClass(); //TODO Replace with class or enum

    //TODO To be completed
}
