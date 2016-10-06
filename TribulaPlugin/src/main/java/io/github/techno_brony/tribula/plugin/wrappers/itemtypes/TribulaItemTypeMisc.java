package io.github.techno_brony.tribula.plugin.wrappers.itemtypes;

public class TribulaItemTypeMisc extends TribulaItemType {

    private static TribulaItemTypeMisc instance;

    public static TribulaItemTypeMisc getInstance() {
        if (instance == null) {
            instance = new TribulaItemTypeMisc();
        }
        return instance;
    }

    @Override
    public String getTypeName() {
        return "Miscellaneous";
    }

}
