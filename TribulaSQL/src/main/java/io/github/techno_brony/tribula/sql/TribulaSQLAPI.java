package io.github.techno_brony.tribula.sql;

import java.util.UUID;

public interface TribulaSQLAPI {
    void getData(final TribulaSQLClient client, final UUID player, final TribulaSQLCallback callback);

    void getData(final TribulaSQLClient client, final UUID player, final String column, final TribulaSQLCallback callback);

    void setData(final TribulaSQLClient client, final UUID player, final String column, final Object value);
}
