package pl.m4code.bot.listener;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import pl.m4code.Main;
import pl.m4code.database.DatabaseConnection;
import pl.m4code.system.MessageManager;
import pl.m4code.utils.TextUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InteractModalListener extends ListenerAdapter {

    private final DatabaseConnection dbConnection = new DatabaseConnection();

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {

        String modalId = event.getModalId();

        if (!modalId.equals("Nagroda")) {
            return;
        }

        String code = event.getValue("Nagroda").getAsString();

        if (code.isEmpty()) {
            event.reply("Błąd: Kod nie może być pusty")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        Long numericCode;
        try {
            numericCode = Long.parseLong(code);
        } catch (NumberFormatException e) {
            event.reply("Błąd: Kod musi być numeryczny")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        String newNickname = dbConnection.getNicknameByCode(numericCode);

        Member member = event.getMember();
        if (member == null || newNickname == null) {
            event.reply("Błąd: Nie znaleziono nagrody o podanym kodzie")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        // Ustawienie statusu gracza
        dbConnection.setConnected(newNickname, true);
        String discordId = member.getId(); // Identyfikator Discord
        String nickname = newNickname; // Nazwa użytkownika
        dbConnection.setDiscordId(discordId, nickname);

        // Pobranie informacji z konfiguracji
        FileConfiguration config = Main.getInstance().getConfig();
        String materialName = config.getString("items.material");
        int amount = config.getInt("items.amount", 1);
        String name = config.getString("items.name");
        List<String> lore = config.getStringList("items.lore");
        List<String> flags = config.getStringList("items.flags");
        int customModelData = config.getInt("items.custom_model_data", 0);
        List<Map<?, ?>> enchantments = config.getMapList("items.enchantments");

        // Sprawdzenie i utworzenie materiału
        Material material = Material.getMaterial(materialName);
        if (material == null) {
            event.reply("Błąd: Niepoprawny materiał w konfiguracji: " + materialName)
                    .setEphemeral(true)
                    .queue();
            return;
        }

        // Utworzenie ItemStack
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();

        // Ustawienie nazwy
        if (name != null && !name.isEmpty()) {
            meta.setDisplayName(TextUtil.fixColor(name));
        }

        // Ustawienie lore
        if (lore != null && !lore.isEmpty()) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, TextUtil.fixColor(lore.get(i)));
            }
            meta.setLore(lore);
        }

        // Ustawienie flag
        if (flags != null && !flags.isEmpty()) {
            for (String flag : flags) {
                try {
                    ItemFlag itemFlag = ItemFlag.valueOf(flag.toUpperCase());
                    meta.addItemFlags(itemFlag);
                } catch (IllegalArgumentException e) {
                    event.reply("Błąd: Niepoprawna flaga przedmiotu: " + flag)
                            .setEphemeral(true)
                            .queue();
                    return;
                }
            }
        }

        // Ustawienie custom model data
        if (customModelData > 0) {
            meta.setCustomModelData(customModelData);
        }

        // Ustawienie enchantments
        for (Map<?, ?> enchantment : enchantments) {
            String enchantName = (String) enchantment.get("name");
            int enchantLevel = (Integer) enchantment.get("level");

            Enchantment enchantmentEnum = Enchantment.getByKey(NamespacedKey.minecraft(enchantName.toLowerCase()));
            if (enchantmentEnum != null) {
                meta.addEnchant(enchantmentEnum, enchantLevel, true);
            } else {
                event.reply("Błąd: Niepoprawny enchantment: " + enchantName)
                        .setEphemeral(true)
                        .queue();
                return;
            }
        }

        // Zastosowanie meta do ItemStack
        itemStack.setItemMeta(meta);

        // Przekazanie itemu do gracza
        Player player = Bukkit.getServer().getPlayer(newNickname);
        if (player != null) {
            PlayerInventory inventory = player.getInventory();
            inventory.addItem(itemStack);
            String successMessage = MessageManager.getConfig().getString("odbierz.success");
            player.sendMessage(TextUtil.fixColor(successMessage));
        } else {
            event.reply("Błąd: Gracz nie został znaleziony: " + newNickname)
                    .setEphemeral(true)
                    .queue();
            return;
        }

        // Wykonanie komend z konfiguracji
        List<String> commands = config.getStringList("commands");
        for (String command : commands) {
            command = command.replace("%player%", newNickname);
            executeCommandsWithDelay(Collections.singletonList(command));
        }

        // Odpowiedź na interakcję
        String successMessage = MessageManager.getConfig().getString("discord.success");
        successMessage = successMessage.replace("{nick}", newNickname);
        event.reply(successMessage)
                .setEphemeral(true)
                .queue();
    }

    private void executeCommandsWithDelay(List<String> commands) {
        for (int i = 0; i < commands.size(); i++) {
            final String command = commands.get(i);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }, i * 20L);
        }
    }
}
