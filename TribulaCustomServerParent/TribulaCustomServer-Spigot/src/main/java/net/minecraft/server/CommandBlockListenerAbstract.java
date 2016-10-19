package net.minecraft.server;

import com.google.common.base.Joiner;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;

// CraftBukkit start
// CraftBukkit end

public abstract class CommandBlockListenerAbstract implements ICommandListener {

    private static final SimpleDateFormat a = new SimpleDateFormat("HH:mm:ss");
    private final CommandObjectiveExecutor g = new CommandObjectiveExecutor();
    protected org.bukkit.command.CommandSender sender; // CraftBukkit - add sender
    private int b;
    private boolean c = true;
    private IChatBaseComponent d;
    private String e = "";
    private String f = "@";

    public CommandBlockListenerAbstract() {}

    // CraftBukkit start
    public static int executeCommand(ICommandListener sender, org.bukkit.command.CommandSender bSender, String command) {
        org.bukkit.command.SimpleCommandMap commandMap = sender.getWorld().getServer().getCommandMap();
        Joiner joiner = Joiner.on(" ");
        if (command.startsWith("/")) {
            command = command.substring(1);
        }
        String[] args = command.split(" ");
        ArrayList<String[]> commands = new ArrayList<String[]>();

        String cmd = args[0];
        if (cmd.startsWith("minecraft:")) cmd = cmd.substring("minecraft:".length());
        if (cmd.startsWith("bukkit:")) cmd = cmd.substring("bukkit:".length());

        // Block disallowed commands
        if (cmd.equalsIgnoreCase("stop") || cmd.equalsIgnoreCase("kick") || cmd.equalsIgnoreCase("op")
                || cmd.equalsIgnoreCase("deop") || cmd.equalsIgnoreCase("ban") || cmd.equalsIgnoreCase("ban-ip")
                || cmd.equalsIgnoreCase("pardon") || cmd.equalsIgnoreCase("pardon-ip") || cmd.equalsIgnoreCase("reload")) {
            return 0;
        }

        // Handle vanilla commands;
        org.bukkit.command.Command commandBlockCommand = commandMap.getCommand(args[0]);
        if (sender.getWorld().getServer().getCommandBlockOverride(args[0])) {
            commandBlockCommand = commandMap.getCommand("minecraft:" + args[0]);
        }
        if (commandBlockCommand instanceof VanillaCommandWrapper) {
            command = command.trim();
            if (command.startsWith("/")) {
                command = command.substring(1);
            }
            String as[] = command.split(" ");
            as = VanillaCommandWrapper.dropFirstArgument(as);
            if (!commandBlockCommand.testPermission(bSender)) {
                return 0;
            }
            return ((VanillaCommandWrapper) commandBlockCommand).dispatchVanillaCommand(bSender, sender, as);
        }

        // Make sure this is a valid command
        if (commandMap.getCommand(args[0]) == null) {
            return 0;
        }

        commands.add(args);

        // Find positions of command block syntax, if any
        //noinspection deprecation
        WorldServer[] prev = MinecraftServer.getServer().worldServer;
        //noinspection deprecation
        MinecraftServer server = MinecraftServer.getServer();
        server.worldServer = new WorldServer[server.worlds.size()];
        server.worldServer[0] = (WorldServer) sender.getWorld();
        int bpos = 0;
        for (int pos = 1; pos < server.worldServer.length; pos++) {
            WorldServer world = server.worlds.get(bpos++);
            if (server.worldServer[0] == world) {
                pos--;
                continue;
            }
            server.worldServer[pos] = world;
        }
        try {
            ArrayList<String[]> newCommands = new ArrayList<String[]>();
            for (int i = 0; i < args.length; i++) {
                if (PlayerSelector.isPattern(args[i])) {
                    for (String[] command1 : commands) {
                        newCommands.addAll(buildCommands(sender, command1, i));
                    }
                    ArrayList<String[]> temp = commands;
                    commands = newCommands;
                    newCommands = temp;
                    newCommands.clear();
                }
            }
        } finally {
            //noinspection deprecation
            MinecraftServer.getServer().worldServer = prev;
        }

        int completed = 0;

        // Now dispatch all of the commands we ended up with
        for (String[] command1 : commands) {
            try {
                if (commandMap.dispatch(bSender, joiner.join(Arrays.asList(command1)))) {
                    completed++;
                }
            } catch (Throwable exception) {
                if (sender.f() instanceof EntityMinecartCommandBlock) {
                    //noinspection deprecation
                    MinecraftServer.getServer().server.getLogger().log(Level.WARNING, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", sender.getChunkCoordinates().getX(), sender.getChunkCoordinates().getY(), sender.getChunkCoordinates().getZ()), exception);
                } else if (sender instanceof CommandBlockListenerAbstract) {
                    CommandBlockListenerAbstract listener = (CommandBlockListenerAbstract) sender;
                    //noinspection deprecation
                    MinecraftServer.getServer().server.getLogger().log(Level.WARNING, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getChunkCoordinates().getX(), listener.getChunkCoordinates().getY(), listener.getChunkCoordinates().getZ()), exception);
                } else {
                    //noinspection deprecation
                    MinecraftServer.getServer().server.getLogger().log(Level.WARNING, "Unknown CommandBlock failed to handle command", exception);
                }
            }
        }

        return completed;
    }

    private static ArrayList<String[]> buildCommands(ICommandListener sender, String[] args, int pos) {
        ArrayList<String[]> commands = new ArrayList<String[]>();
        java.util.List<EntityPlayer> players = PlayerSelector.getPlayers(sender, args[pos], EntityPlayer.class);

        if (players != null) {
            for (EntityPlayer player : players) {
                if (player.world != sender.getWorld()) {
                    continue;
                }
                String[] command = args.clone();
                command[pos] = player.getName();
                commands.add(command);
            }
        }

        return commands;
    }

    public int k() {
        return this.b;
    }

    public void a(@SuppressWarnings("SameParameterValue") int i) {
        this.b = i;
    }

    public IChatBaseComponent l() {
        return this.d == null ? new ChatComponentText("") : this.d;
    }

    public NBTTagCompound a(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Command", this.e);
        nbttagcompound.setInt("SuccessCount", this.b);
        nbttagcompound.setString("CustomName", this.f);
        nbttagcompound.setBoolean("TrackOutput", this.c);
        if (this.d != null && this.c) {
            nbttagcompound.setString("LastOutput", IChatBaseComponent.ChatSerializer.a(this.d));
        }

        this.g.b(nbttagcompound);
        return nbttagcompound;
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.e = nbttagcompound.getString("Command");
        this.b = nbttagcompound.getInt("SuccessCount");
        if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
            this.f = nbttagcompound.getString("CustomName");
        }

        if (nbttagcompound.hasKeyOfType("TrackOutput", 1)) {
            this.c = nbttagcompound.getBoolean("TrackOutput");
        }

        if (nbttagcompound.hasKeyOfType("LastOutput", 8) && this.c) {
            try {
                this.d = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("LastOutput"));
            } catch (Throwable throwable) {
                this.d = new ChatComponentText(throwable.getMessage());
            }
        } else {
            this.d = null;
        }

        this.g.a(nbttagcompound);
    }

    public boolean a(int i, String s) {
        return i <= 2;
    }

    public String getCommand() {
        return this.e;
    }

    public void setCommand(String s) {
        this.e = s;
        this.b = 0;
    }

    public void a(World world) {
        if (world.isClientSide) {
            this.b = 0;
        } else if ("Searge".equalsIgnoreCase(this.e)) {
            this.d = new ChatComponentText("#itzlipofutzli");
            this.b = 1;
        } else {
            MinecraftServer minecraftserver = this.h();

            if (minecraftserver != null && minecraftserver.M() && minecraftserver.getEnableCommandBlock()) {
                ICommandHandler icommandhandler = minecraftserver.getCommandHandler();

                try {
                    this.d = null;
                    // CraftBukkit start - Handle command block commands using Bukkit dispatcher
                    this.b = executeCommand(this, sender, this.e);
                    // CraftBukkit end
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.a(throwable, "Executing command block");
                    CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Command to be executed");

                    //noinspection unchecked
                    crashreportsystemdetails.a("Command", new CrashReportCallable() {
                        public String a() {
                            return CommandBlockListenerAbstract.this.getCommand();
                        }

                        public Object call() throws Exception {
                            return this.a();
                        }
                    });
                    //noinspection unchecked
                    crashreportsystemdetails.a("Name", new CrashReportCallable() {
                        public String a() {
                            return CommandBlockListenerAbstract.this.getName();
                        }

                        public Object call() throws Exception {
                            return this.a();
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            } else {
                this.b = 0;
            }

        }
    }
    // CraftBukkit end

    public String getName() {
        return this.f;
    }

    public void setName(String s) {
        this.f = s;
    }

    public IChatBaseComponent getScoreboardDisplayName() {
        return new ChatComponentText(this.getName());
    }

    public void sendMessage(IChatBaseComponent ichatbasecomponent) {
        if (this.c && this.getWorld() != null && !this.getWorld().isClientSide) {
            this.d = (new ChatComponentText("[" + CommandBlockListenerAbstract.a.format(new Date()) + "] ")).addSibling(ichatbasecomponent);
            this.i();
        }

    }

    public boolean getSendCommandFeedback() {
        MinecraftServer minecraftserver = this.h();

        return minecraftserver == null || !minecraftserver.M() || minecraftserver.worldServer[0].getGameRules().getBoolean("commandBlockOutput");
    }

    public void a(CommandObjectiveExecutor.EnumCommandResult commandobjectiveexecutor_enumcommandresult, int i) {
        this.g.a(this.h(), this, commandobjectiveexecutor_enumcommandresult, i);
    }

    public abstract void i();

    public void b(@Nullable IChatBaseComponent ichatbasecomponent) {
        this.d = ichatbasecomponent;
    }

    public void a(boolean flag) {
        this.c = flag;
    }

    public boolean n() {
        return this.c;
    }

    public boolean a(EntityHuman entityhuman) {
        if (!entityhuman.dh()) {
            return false;
        } else {
            if (entityhuman.getWorld().isClientSide) {
                entityhuman.a(this);
            }

            return true;
        }
    }

    public CommandObjectiveExecutor o() {
        return this.g;
    }
}
