package org.spigotmc;

import gnu.trove.map.hash.TCustomHashMap;

import java.util.Map;

public class CaseInsensitiveMap<V> extends TCustomHashMap<String, V> {

    public CaseInsensitiveMap() {
        //noinspection unchecked
        super(CaseInsensitiveHashingStrategy.INSTANCE);
    }

    @SuppressWarnings("unused")
    public CaseInsensitiveMap(Map<? extends String, ? extends V> map) {
        //noinspection unchecked
        super(CaseInsensitiveHashingStrategy.INSTANCE, map);
    }
}
