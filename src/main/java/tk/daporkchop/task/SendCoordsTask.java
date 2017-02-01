package tk.daporkchop.task;

import java.util.TimerTask;

import cn.nukkit.Player;
import tk.daporkchop.command.CoordsCommand;

public class SendCoordsTask extends TimerTask {

	@Override
	public void run() {
		for (Player p : CoordsCommand.onCoords)	{
			p.sendTip("&l&ax: " + p.getFloorX() + " y: " + p.getFloorY() + " z: " + p.getFloorZ());
		}
	}
}
