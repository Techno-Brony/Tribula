package io.github.techno_brony.tribula.plugin.wrappers.itemtypes.consumable;

import io.github.techno_brony.tribula.plugin.wrappers.TribulaPlayer;
import io.github.techno_brony.tribula.plugin.wrappers.itemtypes.TribulaItemType;

public abstract class TribulaItemTypeConsumable extends TribulaItemType {

    private TribulaItemTypeConsumable() {
    }

    @Override
    public String getTypeName() {
        return "Consumable";
    }

    public abstract void applyConsumableEffect(TribulaPlayer player);

    //TODO
}
