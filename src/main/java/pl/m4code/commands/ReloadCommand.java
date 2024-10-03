package pl.m4code.commands;

import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.m4code.Main;
import pl.m4code.commands.api.CommandAPI;
import pl.m4code.system.MessageManager;
import pl.m4code.utils.TextUtil;

import java.util.List;

public class ReloadCommand extends CommandAPI {
    private Main plugin = Main.getInstance();

    public ReloadCommand() {
        super(
                "nagroda-config",
                "",
                "",
                "/nagroda-config <reload>",
                List.of()
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            TextUtil.sendMessage(sender, "&cPodana komenda jest dostepna tylko dla graczy!");
            return;
        }

        if (!player.hasPermission("m4code.reload")) {
            TextUtil.sendMessage(sender, "&cNie masz uprawnien do tej komendy!");
            return;
        }

        if (args.length <= 0) {
            TextUtil.sendMessage(sender, "&4Błąd: &cPoprawny format: &7/nagroda-config <reload>");
            return;
        }
        if (args[0].equals("reload")) {
        }
        plugin.reloadConfig();
        MessageManager.reloadConfig();
        TextUtil.sendMessage(sender, "&aPliki konfiguracyjne zostaly przeladowane!");
    }


    @Override
    public List<String> tab(@NonNull Player player, @NotNull @NonNull String[] args) {
        return null;
    }
}