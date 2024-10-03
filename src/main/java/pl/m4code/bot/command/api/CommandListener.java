package pl.m4code.bot.command.api;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        Command command = CommandManager.getCommand(e.getName());
        if (command != null) {
            if (command.isOnlyInGuild() && e.getGuild() == null) {
                e.reply("Ta komenda jest dostepna na serwerach!")
                        .queue();
                return;
            }
            command.execute(e);
        } else {
            e.reply("Wystapil problem podczas przetwarzania polecenia!")
                    .setEphemeral(true)
                    .queue();
        }
    }
}
