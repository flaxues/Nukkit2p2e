package tk.daporkchop.task;

import java.util.TimerTask;

import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import tk.daporkchop.DetectorCommandSender;

public class DetectFrozenTask extends TimerTask {
	
	public long lastRunAt = -1;
	public boolean returnedOutput = false;

	@Override
	public void run() {
		if (lastRunAt != -1)	{
			if (!this.returnedOutput)	{
				Server.getInstance().players.forEach((username, player) -> {
					player.kick(TextFormat.colorize("§4§kI§r§f§c The server seems to have frozen, restarting... §4§kI§r§f"));
				});
				Server.getInstance().getLevels().values().forEach(level -> {
					level.save();
				});
				Server.getInstance().forceShutdown();
				System.exit(0);
			}
		}
		lastRunAt = System.currentTimeMillis();
		Server.getInstance().dispatchCommand(new DetectorCommandSender(this), "list");
		this.returnedOutput = false;
	}
}
