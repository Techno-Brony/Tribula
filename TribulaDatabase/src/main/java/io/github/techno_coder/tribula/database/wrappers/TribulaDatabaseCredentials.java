package io.github.techno_coder.tribula.database.wrappers;

public class TribulaDatabaseCredentials {
    private final String username;
    private final String password;
    private final short port;
    private final String databaseName;
    private final String hostname;

    public TribulaDatabaseCredentials(String username, String password, short port, String databaseName, String hostname) {
        this.username = username;
        this.password = password;
        this.port = port;
        this.databaseName = databaseName;
        this.hostname = hostname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public short getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getHostname() {
        return hostname;
    }
}
