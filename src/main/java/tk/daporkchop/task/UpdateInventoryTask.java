package tk.daporkchop.task;

import java.util.TimerTask;

import cn.nukkit.Player;
import cn.nukkit.Server;

public class UpdateInventoryTask extends TimerTask {

	@Override
	public void run() {
		for (Player p : Server.getInstance().getOnlinePlayers().values())	{
			p.clearHackedItems();
			p.getInventory().sendContents(p);
		}
	}
}
