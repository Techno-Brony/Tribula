package org.spigotmc;

public class SneakyThrow
{

    @SuppressWarnings("unused")
    public static void sneaky(Throwable t)
    {
        throw SneakyThrow.<RuntimeException>superSneaky( t );
    }

    private static <T extends Throwable> T superSneaky(Throwable t) throws T
    {
        //noinspection unchecked
        throw (T) t;
    }
}
