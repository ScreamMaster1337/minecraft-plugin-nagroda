package pl.m4code.bot.command.api;

import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


@Getter
public abstract class Command {


    private final String cmd;

    private final String depression;

    private final boolean onlyInGuild;



    public Command(String cmd, String depression, boolean onlyInGuild) {
        this.cmd = cmd;
        this.depression = depression;
        this.onlyInGuild = onlyInGuild;
        CommandManager.commands.add(this);
        CommandManager.getJda().upsertCommand(cmd, depression).queue();
    }

    public void execute(SlashCommandInteractionEvent e) {}

    public String getCmd() {
        return this.cmd;
    }

    public boolean isOnlyInGuild() {
        return this.onlyInGuild;
    }
}