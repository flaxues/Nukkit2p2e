package tk.daporkchop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import tk.daporkchop.command.CoordsCommand;
import tk.daporkchop.command.GetPosCommand;
import tk.daporkchop.command.MuteCommand;
import tk.daporkchop.command.RotCommand;
import tk.daporkchop.command.VoteCommand;
import tk.daporkchop.command.VoterewardCommand;
import tk.daporkchop.task.AutoRestartTask;
import tk.daporkchop.task.MessageRead;
import tk.daporkchop.task.RandomMessagesTask;
import tk.daporkchop.task.SendCoordsTask;
import tk.daporkchop.task.SendMessagesToDiscordTask;
import tk.daporkchop.task.UpdatePlayerCountTask;

public class PorkUtils extends PluginBase {
	
	/**
	 * Essentially a list of players i want muted because thy are fags
	 */
	public static final ArrayList<String> muted = new ArrayList<String>();
	
	static {
		muted.add("zLord47");
		muted.add("InfernoViper");
	}
	
	public static JDA api;
	public static TextChannel minecraftChannel;
	public static String[] welcomeMessage;
	
	public static ArrayList<String> queuedMessages = new ArrayList<String>();
	
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
		
		final String tkn = token; //fuck final variables
		(new Thread() {
			@Override
			public void run()	{
				try {
					api = new JDABuilder(AccountType.BOT).setToken(tkn).buildBlocking();
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
			}
		}).start();
		
        new Timer().schedule(new MessageRead(), 0, 7200000);
        new Timer().schedule(new UpdatePlayerCountTask(), 5000, 5000);
        new Timer().schedule(new RandomMessagesTask(), 1000, 120000);
        new Timer().schedule(new AutoRestartTask(), 0, 1000);
        new Timer().schedule(new SendMessagesToDiscordTask(), 5000, 1000);
        new Timer().schedule(new SendCoordsTask(), 10000, 250);
        
        SimpleCommandMap.INSTANCE.register("nukkit", new GetPosCommand("getpos"));
        SimpleCommandMap.INSTANCE.register("nukkit", new AnnounceCommand("announce"));
        SimpleCommandMap.INSTANCE.register("nukkit", new VoterewardCommand("votereward"));
        SimpleCommandMap.INSTANCE.register("nukkit", new VoteCommand("vote"));
        SimpleCommandMap.INSTANCE.register("nukkit", new CoordsCommand("coords"));
        SimpleCommandMap.INSTANCE.register("nukkit", new RotCommand("rot"));
        SimpleCommandMap.INSTANCE.register("nukkit", new MuteCommand("mute"));
        
        try {
			new WebServer(new Timer());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * Queues a message to be sent to the discord
	 * @param s
	 * @return
	 */
	public static String queueMessageForDiscord(String s)	{
		queuedMessages.add(s + "\n");
		return s;
	}
	
	/**
	 * Queues a message to be sent to the discord from a player
	 * @param s
	 * @return
	 */
	public static String queueMessageForDiscord(String s, Player p)	{
		queuedMessages.add(s = ("[" + p.getName() + "] " + s + "\n"));
		return s;
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
	@Deprecated
	public static void sendMessageToDiscord(String msg, Player p) {
		minecraftChannel.sendMessage("[" + p.getName() + "] " + msg).queue();
	}

	/**
	 * Sends a message to #minecraft-chat on the 2p2e Discord.
	 * @param message
	 */
	@Deprecated
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
