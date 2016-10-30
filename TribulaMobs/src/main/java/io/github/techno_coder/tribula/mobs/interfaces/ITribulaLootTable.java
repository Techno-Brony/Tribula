package io.github.techno_coder.tribula.mobs.interfaces;

import io.github.techno_coder.tribula.internals.enums.TribulaPlayerDifficulty;

public interface ITribulaLootTable { //TODO
    void getRandomItem();
    void getRandomItem(TribulaPlayerDifficulty difficulty);
    void getRandomItems(int amount);
    void getRandomItems(int amount, TribulaPlayerDifficulty difficulty);
}
