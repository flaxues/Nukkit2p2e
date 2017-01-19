package tk.daporkchop.task;

import java.util.TimerTask;

import cn.nukkit.Server;
import tk.daporkchop.PorkUtils;

public class UpdatePlayerCountTask extends TimerTask {

	boolean isFirstCycle = true;

	@Override
	public void run() {
		PorkUtils.changePlayerCount(Server.getInstance().getOnlinePlayers().size(),
				Server.getInstance().getMaxPlayers());
		if (this.isFirstCycle)	{
			PorkUtils.sendMessageToDiscord("2p2e is back up and running!");
			this.isFirstCycle = false;
		}
	}

}