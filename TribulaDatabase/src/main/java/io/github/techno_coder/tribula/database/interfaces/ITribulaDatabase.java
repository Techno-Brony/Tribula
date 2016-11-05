package io.github.techno_coder.tribula.database.interfaces;

import java.sql.Connection;

public interface ITribulaDatabase {
    void openConnection();
    Connection getConnection();
    void closeConnection();
}
