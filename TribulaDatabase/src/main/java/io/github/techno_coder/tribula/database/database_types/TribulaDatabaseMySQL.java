package io.github.techno_coder.tribula.database.database_types;

import io.github.techno_coder.tribula.database.Main;
import io.github.techno_coder.tribula.database.interfaces.ITribulaDatabase;
import io.github.techno_coder.tribula.database.wrappers.TribulaDatabaseCredentials;

public class TribulaDatabaseMySQL implements ITribulaDatabase {

    private final Main plugin;
    private final TribulaDatabaseCredentials credentials;

    public TribulaDatabaseMySQL(Main plugin, TribulaDatabaseCredentials credentials) {
        this.plugin = plugin;
        this.credentials = credentials;
    }

    @Override
    public void openConnection() {

    }
}
