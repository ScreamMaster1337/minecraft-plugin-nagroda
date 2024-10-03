package pl.m4code.bot.listener;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import pl.m4code.database.DatabaseConnection;
import pl.m4code.system.MessageManager;

public class InteractButtonListener extends ListenerAdapter {

    DatabaseConnection dbConnection = new DatabaseConnection();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Button button = event.getButton();
        String id = button.getId();
        String discordId = event.getUser().getId();
        if (id == null) return;
        if (event.getMessage() == null) {
            return;
        }

        if (id.equals("nagroda-x")) {
            if (dbConnection.isUserConnected(event.getUser().getName())) {
                String errorMessage = MessageManager.getConfig().getString("discord.error");
                event.reply(errorMessage).setEphemeral(true).queue();
                return;
            }
            if (dbConnection.isDiscordUserExist(discordId)) {
                String errorMessage = MessageManager.getConfig().getString("discord.error-dc");
                event.reply(errorMessage)
                        .setEphemeral(true)
                        .queue();
                return;
            }

            TextInput text = TextInput.create("Nagroda", "Wprowadź kod", TextInputStyle.SHORT)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("Nagroda", "Wprowadź kod")
                    .addActionRows(ActionRow.of(text))
                    .build();

            try {
                event.replyModal(modal).queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
