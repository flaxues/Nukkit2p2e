package tk.daporkchop.task;

import java.util.TimerTask;

import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;

public class AutoRestartTask extends TimerTask {
	
	public int loopCount = 0;

	@Override
	public void run() {
		loopCount++;
		if (loopCount == 6 * 60 * 60 - 10)	{
			Server.getInstance().broadcastMessage(TextFormat.colorize("&l&cSERVER RESTARTING IN 10"));
		} else if (loopCount == 6 * 60 * 60 - 9)	{
			Server.getInstance().broadcastMessage(TextFormat.colorize("&l&cSERVER RESTARTING IN 9"));
		} else if (loopCount == 6 * 60 * 60 - 8)	{
			Server.getInstance().broadcastMessage(TextFormat.colorize("&l&cSERVER RESTARTING IN 8"));
		} else if (loopCount == 6 * 60 * 60 - 7)	{
			Server.getInstance().broadcastMessage(TextFormat.colorize("&l&cSERVER RESTARTING IN 7"));
		} else if (loopCount == 6 * 60 * 60 - 6)	{
			Server.getInstance().broadcastMessage(TextFormat.colorize("&l&cSERVER RESTARTING IN 6"));
		} else if (loopCount == 6 * 60 * 60 - 5)	{
			Server.getInstance().broadcastMessage(TextFormat.colorize("&l&cSERVER RESTARTING IN 5"));
		} else if (loopCount == 6 * 60 * 60 - 4)	{
			Server.getInstance().broadcastMessage(TextFormat.colorize("&l&cSERVER RESTARTING IN 4"));
		} else if (loopCount == 6 * 60 * 60 - 3)	{
			Server.getInstance().broadcastMessage(TextFormat.colorize("&l&cSERVER RESTARTING IN 3"));
		} else if (loopCount == 6 * 60 * 60 - 2)	{
			Server.getInstance().broadcastMessage(TextFormat.colorize("&l&cSERVER RESTARTING IN 2"));
		} else if (loopCount == 6 * 60 * 60 - 1)	{
			Server.getInstance().broadcastMessage(TextFormat.colorize("&l&cSERVER RESTARTING IN 1"));
		} else if (loopCount == 6 * 60 * 60)	{
			Server.getInstance().broadcastMessage(TextFormat.colorize("&l&cSERVER RESTARTING IN 0"));
			Server.getInstance().shutdown(TextFormat.colorize("&k&4I&r &l&cSERVER RESTARTING &r&k&4I"));
		}
	}
	
	public AutoRestartTask setLoopCount(int count)	{
		this.loopCount = count;
		return this;
	}

}
