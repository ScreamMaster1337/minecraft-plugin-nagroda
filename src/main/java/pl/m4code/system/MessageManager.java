package pl.m4code.system;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.m4code.Main;

import java.io.File;

public class MessageManager {
    private static FileConfiguration config;
    private static File configFile;

    static {
        configFile = new File(Main.getInstance().getDataFolder(), "messages.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            Main.getInstance().saveResource("messages.yml", false);
        }
        reloadConfig();
    }


    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static FileConfiguration getConfig() {
        return config;
    }
}
