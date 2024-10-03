package pl.m4code.service;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import pl.m4code.Main;
import pl.m4code.bot.command.NagrodaCommand;
import pl.m4code.bot.command.api.CommandManager;
import pl.m4code.bot.listener.InteractButtonListener;
import pl.m4code.bot.listener.InteractModalListener;
import pl.m4code.utils.TextUtil;

import static net.dv8tion.jda.api.OnlineStatus.ONLINE;

@Getter
public class BotService {

    private static BotService instance;
    private JDA jda;

    public static BotService getInstance() {
        if (instance == null) instance = new BotService();
        return instance;
    }

    public void setupBot() {
        try {
            String token = Main.getInstance().getConfig().getString("bot.token");
            String activityType = Main.getInstance().getConfig().getString("bot.activity.type");
            String activityName = Main.getInstance().getConfig().getString("bot.activity.name");

            Activity.ActivityType activityTypeEnum;
            try {
                activityTypeEnum = Activity.ActivityType.valueOf(activityType.toUpperCase());
            } catch (IllegalArgumentException e) {
                activityTypeEnum = Activity.ActivityType.PLAYING; // Default value
            }

            jda = JDABuilder.createDefault(token)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setStatus(ONLINE)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .setActivity(Activity.of(activityTypeEnum, activityName))
                    .build();

            jda.addEventListener(
                    new InteractButtonListener(),
                    new InteractModalListener()
            );

            CommandManager.registerManager(jda);
            new NagrodaCommand();
            TextUtil.sendLogger("Zalogowano jako " + jda.getSelfUser().getAsTag());
        } catch (Exception e) {
            TextUtil.sendLogger("Wystapil problem podczas logowania bota!" + e);
        }
    }
}
