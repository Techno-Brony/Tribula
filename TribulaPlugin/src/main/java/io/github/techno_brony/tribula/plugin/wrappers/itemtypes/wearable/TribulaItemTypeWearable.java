package io.github.techno_brony.tribula.plugin.wrappers.itemtypes.wearable;

import io.github.techno_brony.tribula.plugin.wrappers.itemtypes.TribulaItemType;

public abstract class TribulaItemTypeWearable extends TribulaItemType {

    private TribulaItemTypeWearable() {
    }

    @Override
    public String getTypeName() {
        return "Wearable";
    }

    public abstract void applyWearableEffect();

    public abstract void removeWearableEffect();

    //TODO
}
