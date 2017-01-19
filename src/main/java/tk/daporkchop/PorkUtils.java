package tk.daporkchop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Timer;

import javax.security.auth.login.LoginException;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.GameImpl;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import tk.daporkchop.command.AnnounceCommand;
import tk.daporkchop.command.GetPosCommand;
import tk.daporkchop.command.VoteCommand;
import tk.daporkchop.command.VoterewardCommand;
import tk.daporkchop.task.AutoRestartTask;
import tk.daporkchop.task.MessageRead;
import tk.daporkchop.task.RandomMessagesTask;
import tk.daporkchop.task.UpdatePlayerCountTask;

public class PorkUtils extends PluginBase {
	
	public static JDA api;
	public static TextChannel minecraftChannel;
	public static String[] welcomeMessage;
	
	@Override
    public void onEnable() {
        this.getLogger().info("PorkUtils started!");
        File f = new File(System.getProperty("user.dir") + "/discordtoken.txt");
		String token = "";

		if (!f.exists()) {
			try {
				PrintWriter writer = new PrintWriter(f.getAbsolutePath(), "UTF-8");
				Scanner s = new Scanner(System.in);

				System.out.println("Please enter your discord bot token");
				token = s.nextLine();
				writer.println(token);
				System.out.println("Successful. Starting...");

				s.close();
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Scanner s = new Scanner(f);

				token = s.nextLine();

				s.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		token = token.trim();
		try {
			api = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
			minecraftChannel = api.getTextChannelById("259327833535414272");
			api.addEventListener(new DiscordEventHandler());
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RateLimitedException e) {
			e.printStackTrace();
		}
        Timer timer = new Timer();
        
        timer.schedule(new MessageRead(), 0, 7200000);
        timer.schedule(new UpdatePlayerCountTask(), 5000, 5000);
        timer.schedule(new RandomMessagesTask(), 1000, 120000);
        timer.schedule(new AutoRestartTask(), 0, 1000);
        
        SimpleCommandMap.INSTANCE.register("nukkit", new GetPosCommand("getpos"));
        SimpleCommandMap.INSTANCE.register("nukkit", new AnnounceCommand("announce"));
        SimpleCommandMap.INSTANCE.register("nukkit", new VoterewardCommand("votereward"));
        SimpleCommandMap.INSTANCE.register("nukkit", new VoteCommand("vote"));
        
        try {
			new WebServer(timer);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * Changes the player count number under the bot's name
	 * @param curr
	 * @param max
	 */
	public static void changePlayerCount(int curr, int max) {
		api.getPresence().setGame(new GameImpl("Online: " + curr + "/" + max, "", GameType.DEFAULT));
	}

	/**
	 * Sends a message to #minecraft-chat on the 2p2e Discord from a player.
	 * @param msg
	 * @param p
	 */
	public static void sendMessageToDiscord(String msg, Player p) {
		minecraftChannel.sendMessage("[" + p.getName() + "] " + msg).queue();
	}

	/**
	 * Sends a message to #minecraft-chat on the 2p2e Discord.
	 * @param message
	 */
	public static void sendMessageToDiscord(String message) {
		minecraftChannel.sendMessage(message).queue();
	}

	/**
	 * Sends a message to 2p2e's ingame chat, from a User.
	 * @param msg
	 * @param user
	 */
	public static void sendMessageToServer(String msg, User user) {
		Server.getInstance().broadcastMessage(TextFormat.colorize((user.getId().equals("226975061880471552") ? "&l&0[&4Owner&0] " : "&l&0") + "[&9Discord&0] &r&f[" + user.getName() + "] " + msg));
	}
}
