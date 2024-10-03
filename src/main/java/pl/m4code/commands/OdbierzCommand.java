package pl.m4code.commands;

import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.m4code.commands.api.CommandAPI;
import pl.m4code.database.DatabaseConnection;
import pl.m4code.system.MessageManager;
import pl.m4code.utils.TextUtil;

import java.util.List;
import java.util.Random;

public class OdbierzCommand extends CommandAPI {

    public OdbierzCommand() {
        super(
                "odbierz",
                "",
                "",
                "/odbierz",
                List.of("odbierz")
        );
    }

    private final DatabaseConnection dbConnection = new DatabaseConnection();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            TextUtil.sendMessage(sender, "&cPodana komenda jest dostępna tylko dla graczy!");
            return;
        }

        if (dbConnection.isMcConnected(player.getName())) {
            String connectedMessage = MessageManager.getConfig().getString("odbierz.error");
            player.sendMessage(TextUtil.fixColor(connectedMessage));
            return;
        }

        if (dbConnection.isCodeExist(player.getName())) {
            int codee = generateRandom();
            String connectedMessage = MessageManager.getConfig().getString("odbierz.code_message1");
            connectedMessage = connectedMessage.replace("{kod}", String.valueOf(codee));
            String connectedMessage_2 = MessageManager.getConfig().getString("odbierz.code_message2");
            connectedMessage_2 = connectedMessage_2.replace("{kod}", String.valueOf(codee));
            player.sendMessage(TextUtil.fixColor(connectedMessage));
            player.sendMessage(TextUtil.fixColor(connectedMessage_2));
            dbConnection.setCode(player.getUniqueId(), codee);
            return;
        }

        int codee = generateRandom();
        String connectedMessage = MessageManager.getConfig().getString("odbierz.code_message1");
        connectedMessage = connectedMessage.replace("{kod}", String.valueOf(codee));
        String connectedMessage_2 = MessageManager.getConfig().getString("odbierz.code_message2");
        connectedMessage_2 = connectedMessage_2.replace("{kod}", String.valueOf(codee));
        String connectedMessage_3 = MessageManager.getConfig().getString("odbierz.code_title");
        connectedMessage_3 = connectedMessage_3.replace("{kod}", String.valueOf(codee));
        player.sendMessage(TextUtil.fixColor(connectedMessage));
        player.sendMessage(TextUtil.fixColor(connectedMessage_2));
        player.sendTitle("", TextUtil.fixColor(connectedMessage_3));

        dbConnection.createUser(player.getUniqueId(), player.getName(), false, codee);
    }

    private int generateRandom() {
        Random random = new Random();
        int min = 10000;
        int max = 99999;
        return random.nextInt(max - min + 1) + min;
    }

    @Override
    public List<String> tab(@NonNull Player player, @NotNull @NonNull String[] args) {
        return null;
    }
}
