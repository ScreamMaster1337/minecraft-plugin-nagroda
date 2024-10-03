package pl.m4code.bot.command.api;

import net.dv8tion.jda.api.JDA;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public static List<Command> commands = new ArrayList<>();

    private static JDA jda;

    public static JDA getJda() {
        return jda;
    }

    public static void registerManager(JDA jda) {
        CommandManager.jda = jda;
        jda.addEventListener(new CommandListener());
    }

    public static Command getCommand(String cmd) {
        for (Command command : commands) {
            if (command.getCmd().equals(cmd))
                return command;
        }
        return null;
    }
}
