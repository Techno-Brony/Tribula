package io.github.techno_coder.tribula.internals.interfaces;

import io.github.techno_coder.tribula.internals.wrappers.TribulaSpecial;
import net.minecraft.server.PathfinderGoal;

public interface ITribulaMobAPI {

    /**
     * Adds a special attack to this mob
     * @param special A pathfinder goal
     * @return True if it was successful
     * @see net.minecraft.server.PathfinderGoal
     */
    boolean addSpecial(TribulaSpecial special);

    /**
     * Adds a pathfinder goal to this mob
     * @param goal A pathfinder goal
     * @return True if it was successful
     * @see net.minecraft.server.PathfinderGoal
     */
    boolean addAI(PathfinderGoal goal);
}
