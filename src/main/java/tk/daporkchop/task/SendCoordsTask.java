package tk.daporkchop.task;

import java.util.TimerTask;

import cn.nukkit.Player;
import tk.daporkchop.command.CoordsCommand;
import tk.daporkchop.command.RotCommand;

public class SendCoordsTask extends TimerTask {

	@Override
	public void run() {
		for (Player p : CoordsCommand.onCoords)	{
			p.sendTip("\n\n&l&ax: " + p.getFloorX() + " y: " + p.getFloorY() + " z: " + p.getFloorZ());
		}
		for (Player p : RotCommand.onRotation)	{
			p.sendTip("\n\n&l&ayaw: " + Math.round(p.getYaw() * 1000d) / 1000d + " pitch: " + Math.round(p.getPitch() * 1000d) / 1000d);
		}
	}
}
