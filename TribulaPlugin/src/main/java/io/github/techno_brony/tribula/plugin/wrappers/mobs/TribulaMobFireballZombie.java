package io.github.techno_brony.tribula.plugin.wrappers.mobs;

import io.github.techno_brony.tribula.plugin.wrappers.mobs.pathfindergoals.TribulaPathfinderGoalFireball;
import net.minecraft.server.v1_10_R1.*;

import java.util.Set;

import static io.github.techno_brony.tribula.plugin.wrappers.enums.TribulaCustomEntityTypes.getPrivateField;

public class TribulaMobFireballZombie extends EntityZombie {

    public TribulaMobFireballZombie(World world) {
        super(world);

        Set goalB = (Set) getPrivateField("b", PathfinderGoalSelector.class, goalSelector);
        goalB.clear();
        Set goalC = (Set) getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
        goalC.clear();
        Set targetB = (Set) getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
        targetB.clear();
        Set targetC = (Set) getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
        targetC.clear();

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(new EntitySpider(world), 1.0D, false));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.goalSelector.a(4, new TribulaPathfinderGoalFireball(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntitySpider.class, true));
    }
}
