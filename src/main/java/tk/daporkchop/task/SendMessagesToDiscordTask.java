package tk.daporkchop.task;

import java.util.Iterator;
import java.util.TimerTask;

import tk.daporkchop.PorkUtils;

public class SendMessagesToDiscordTask extends TimerTask {

	@Override
	public void run() {
		String toSend = "", temp = "", otherTemp = null;
		Iterator<String> iter = PorkUtils.queuedMessages.iterator();
		ESCAPE: while (iter.hasNext())	{
			temp += (otherTemp = iter.next()) + "\n";
			if (temp.length() > 2000)	{ //Message is too long to be sent in one message
				break ESCAPE;
			}
			toSend += otherTemp;
		}
		PorkUtils.queuedMessages.clear(); //I know that this might discard some messages, but if there are more than 2000 characters showing up in chat at the same time something is wrong
		PorkUtils.minecraftChannel.sendMessage(toSend.substring(0, toSend.length() - 2)).queue();
	}

}
