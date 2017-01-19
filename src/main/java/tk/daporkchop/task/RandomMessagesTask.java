package tk.daporkchop.task;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;

public class RandomMessagesTask extends TimerTask {
	
	public static final ArrayList<String> randomMessages = new ArrayList<String>();

	@Override
	public void run() {
		Server.getInstance().broadcastMessage(TextFormat.colorize(randomMessages.get(new Random().nextInt(randomMessages.size()))));
	}
}
