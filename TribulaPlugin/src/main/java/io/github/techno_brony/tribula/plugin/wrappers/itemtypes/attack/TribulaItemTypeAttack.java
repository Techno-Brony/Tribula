package io.github.techno_brony.tribula.plugin.wrappers.itemtypes.attack;

import io.github.techno_brony.tribula.plugin.wrappers.itemtypes.TribulaItemType;

public abstract class TribulaItemTypeAttack extends TribulaItemType {

    private TribulaItemTypeAttack() {
    }

    @Override
    public String getTypeName() {
        return "Attack";
    }

    public abstract double getDamage();

    //TODO
}
