package net.minecraft.server;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class CommandTp extends CommandAbstract {

    public CommandTp() {}

    private static void a(Entity entity, CommandAbstract.CommandNumber commandabstract_commandnumber, CommandAbstract.CommandNumber commandabstract_commandnumber1, CommandAbstract.CommandNumber commandabstract_commandnumber2, CommandAbstract.CommandNumber commandabstract_commandnumber3, CommandAbstract.CommandNumber commandabstract_commandnumber4) {
        float f;

        if (entity instanceof EntityPlayer) {
            EnumSet enumset = EnumSet.noneOf(PacketPlayOutPosition.EnumPlayerTeleportFlags.class);

            if (commandabstract_commandnumber.c()) {
                //noinspection unchecked
                enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.X);
            }

            if (commandabstract_commandnumber1.c()) {
                //noinspection unchecked
                enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y);
            }

            if (commandabstract_commandnumber2.c()) {
                //noinspection unchecked
                enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z);
            }

            if (commandabstract_commandnumber4.c()) {
                //noinspection unchecked
                enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT);
            }

            if (commandabstract_commandnumber3.c()) {
                //noinspection unchecked
                enumset.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT);
            }

            f = (float) commandabstract_commandnumber3.b();
            if (!commandabstract_commandnumber3.c()) {
                f = MathHelper.g(f);
            }

            float f1 = (float) commandabstract_commandnumber4.b();

            if (!commandabstract_commandnumber4.c()) {
                f1 = MathHelper.g(f1);
            }

            entity.stopRiding();
            //noinspection unchecked
            ((EntityPlayer) entity).playerConnection.a(commandabstract_commandnumber.b(), commandabstract_commandnumber1.b(), commandabstract_commandnumber2.b(), f, f1, enumset);
            entity.h(f);
        } else {
            float f2 = (float) MathHelper.g(commandabstract_commandnumber3.a());

            f = (float) MathHelper.g(commandabstract_commandnumber4.a());
            f = MathHelper.a(f, -90.0F, 90.0F);
            entity.setPositionRotation(commandabstract_commandnumber.a(), commandabstract_commandnumber1.a(), commandabstract_commandnumber2.a(), f2, f);
            entity.h(f2);
        }

        if (!(entity instanceof EntityLiving) || !((EntityLiving) entity).cG()) {
            entity.motY = 0.0D;
            entity.onGround = true;
        }

    }

    public String getCommand() {
        return "tp";
    }

    public int a() {
        return 2;
    }

    public String getUsage(ICommandListener icommandlistener) {
        return "commands.tp.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandListener icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new ExceptionUsage("commands.tp.usage");
        } else {
            byte b0 = 0;
            Object object;

            if (astring.length != 2 && astring.length != 4 && astring.length != 6) {
                object = a(icommandlistener);
            } else {
                object = b(minecraftserver, icommandlistener, astring[0]);
                b0 = 1;
            }

            if (astring.length != 1 && astring.length != 2) {
                if (astring.length < b0 + 3) {
                    throw new ExceptionUsage("commands.tp.usage");
                } else if (((Entity) object).world != null) {
                    boolean flag = true;
                    int i = b0 + 1;
                    CommandAbstract.CommandNumber commandabstract_commandnumber = a(((Entity) object).locX, astring[b0], true);
                    CommandAbstract.CommandNumber commandabstract_commandnumber1 = a(((Entity) object).locY, astring[i++], -4096, 4096, false);
                    CommandAbstract.CommandNumber commandabstract_commandnumber2 = a(((Entity) object).locZ, astring[i++], true);
                    CommandAbstract.CommandNumber commandabstract_commandnumber3 = a((double) ((Entity) object).yaw, astring.length > i ? astring[i++] : "~", false);
                    CommandAbstract.CommandNumber commandabstract_commandnumber4 = a((double) ((Entity) object).pitch, astring.length > i ? astring[i] : "~", false);

                    a((Entity) object, commandabstract_commandnumber, commandabstract_commandnumber1, commandabstract_commandnumber2, commandabstract_commandnumber3, commandabstract_commandnumber4);
                    a(icommandlistener, this, "commands.tp.success.coordinates", ((Entity) object).getName(), commandabstract_commandnumber.a(), commandabstract_commandnumber1.a(), commandabstract_commandnumber2.a());
                }
            } else {
                Entity entity = b(minecraftserver, icommandlistener, astring[astring.length - 1]);

                // CraftBukkit Start
                // Use Bukkit teleport method in all cases. It has cross dimensional handling, events
                if (((Entity) object).getBukkitEntity().teleport(entity.getBukkitEntity(), org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND)) {
                    a(icommandlistener, this, "commands.tp.success", ((Entity) object).getName(), entity.getName());
                    // CraftBukkit End
                }
            }
        }
    }

    public List<String> tabComplete(MinecraftServer minecraftserver, ICommandListener icommandlistener, String[] astring, @Nullable BlockPosition blockposition) {
        return astring.length != 1 && astring.length != 2 ? Collections.<String>emptyList() : a(astring, minecraftserver.getPlayers()); // CraftBukkit - decompile error
    }

    public boolean isListStart(String[] astring, int i) {
        return i == 0;
    }

    // CraftBukkit start - fix decompile error
    @Override
    public int compareTo(ICommand o) {
        return a(o);
    }
    // CraftBukkit end
}
