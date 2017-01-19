package tk.daporkchop.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TimerTask;

import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import tk.daporkchop.PorkUtils;

public class MessageRead extends TimerTask {

	@Override
	public void run() {
		this.readMessages();
	}

	public void readMessages() {
		File f = new File(System.getProperty("user.dir") + "/messages.txt");
		Scanner s = null;

		try {
			s = new Scanner(f);
		} catch (FileNotFoundException e) {
			Server.getInstance().getLogger().error("messages.txt not found, aborting");
			System.exit(1);
		}
		PorkUtils.welcomeMessage = TextFormat.colorize(s.nextLine()).split("@"); // Get welcome message
		for (String a : PorkUtils.welcomeMessage) {
			Server.getInstance().getLogger().info(a);
		}
		
		RandomMessagesTask.randomMessages.clear();
		while (s.hasNextLine())	{
			RandomMessagesTask.randomMessages.add(TextFormat.colorize(s.nextLine()));
		}

		Server.getInstance().getLogger().info("Done reading from messages.txt");
	}

}
