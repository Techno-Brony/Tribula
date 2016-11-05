package io.github.techno_coder.tribula.database.builders;

import org.bukkit.Bukkit;

public abstract class TribulaStatementBuilder {



    private String sanitizeSQLInput(String string) {
        String stringCopy = string.toUpperCase();
        if (stringCopy.contains(";")) {
            Bukkit.getLogger().severe("SQL injection detected with string : " + string);
            string = "";
        } else if (stringCopy.contains("DROP") || stringCopy.contains("TRUNCATE")) {
            Bukkit.getLogger().severe("SQL removal operation detected with string : " + string);
            string = "";
        }
        return string;
    }

}
