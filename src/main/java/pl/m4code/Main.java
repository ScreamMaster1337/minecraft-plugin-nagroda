package pl.m4code;

import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import pl.m4code.commands.OdbierzCommand;

import pl.m4code.commands.ReloadCommand;
import pl.m4code.commands.api.CommandAPI;
import pl.m4code.database.DatabaseConnection;

import pl.m4code.service.BotService;
import pl.m4code.system.MessageManager;


import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.List;

@Getter
public final class Main extends JavaPlugin {
    @Getter private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static InventoryManager getInvManager() {
        return null;
    }


    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new MessageManager();
        instance = this;
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.getConnection();

        if (connection != null) {
            dbConnection.createTableIfNotExists();
        }
        BotService.getInstance().setupBot();
        try {
            registerCommands();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        registerListeners();
        registerTasks();

    }

    @SneakyThrows
    private void registerCommands() throws NoSuchFieldException, IllegalAccessException {
        final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        final CommandMap commandMap = (CommandMap) bukkitCommandMap.get(getServer());
        for (CommandAPI commands : List.of(
                new OdbierzCommand(),
                new ReloadCommand()
        )) {
            commandMap.register(commands.getName(), commands);
        }
    }

    private void registerListeners() {
    }

    private void registerTasks() {
    }
}
