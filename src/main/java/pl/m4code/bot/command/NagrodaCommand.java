package pl.m4code.bot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import pl.m4code.Main;
import pl.m4code.bot.command.api.Command;
import pl.m4code.system.MessageManager;

import java.awt.*;

public class NagrodaCommand extends Command {

    String ROLE = Main.getInstance().getConfig().getString("bot.allowed_role");

    public NagrodaCommand() {
        super(
                "polacz",
                "Komenda służąca do wysłania wiadomości embed!",
                true
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (event.getMember() == null || !event.getMember().getRoles().stream().anyMatch(role -> role.getId().equals(ROLE))) {
            event.reply("Nie masz odpowiednich uprawnień, aby użyć tej komendy.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        String title = MessageManager.getConfig().getString("embed.title");
        String color = MessageManager.getConfig().getString("embed.color");
        String description = MessageManager.getConfig().getString("embed.description");
        String footer = MessageManager.getConfig().getString("embed.footer");
        String label = MessageManager.getConfig().getString("embed.label");
        String style = MessageManager.getConfig().getString("embed.style");
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setDescription(description);
        embed.setColor(Color.decode(color));
        embed.setFooter(footer);

        Button button;
        switch (style.toLowerCase()) {
            case "danger":
                button = Button.danger("nagroda-x", label);
                break;
            case "primary":
                button = Button.primary("nagroda-x", label);
                break;
            case "secondary":
                button = Button.secondary("nagroda-x", label);
                break;
            default:
                button = Button.primary("nagroda-x", label);
                break;
        }

        event.reply("Pomyślnie wysłano wiadomość embed")
                .setEphemeral(true)
                .queue(reply -> {
                    event.getChannel().sendMessageEmbeds(embed.build())
                            .setActionRow(button)
                            .queue();
                });
    }
}
