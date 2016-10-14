package io.github.techno_brony.tribula.plugin.wrappers.specials;

import io.github.techno_brony.tribula.plugin.wrappers.enums.TribulaSpecialType;
import io.github.techno_brony.tribula.plugin.wrappers.interfaces.ITribulaSpecial;

public abstract class TribulaSpecialSelf implements ITribulaSpecial {

    @Override
    public TribulaSpecialType getSpecialType() {
        return TribulaSpecialType.SELF;
    }

}
