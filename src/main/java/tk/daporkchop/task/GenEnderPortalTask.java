package tk.daporkchop.task;

import java.util.TimerTask;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.potion.Effect;

public class GenEnderPortalTask extends TimerTask {

	public Position pos;
	public Player player;
	public int count = 0;
	public Server s;

	public GenEnderPortalTask(Position pos, Player player) {
		this.pos = pos;
		this.player = player;
		Effect effect = Effect.getEffect(Effect.ABSORPTION);
		effect.setAmbient(false);
		effect.setDuration(Integer.MAX_VALUE);
		effect.setAmplifier(127);
		player.addEffect(effect);
		this.s = Server.getInstance();
	}

	public GenEnderPortalTask(int x, int y, int z, Level lvl, Player player) {
		this.pos = new Position(x, y, z, lvl);
		this.player = player;
	}

	@Override
	public void run() {
		if (++count < 30) {
			player.teleport(pos);
			s.getScheduler().scheduleDelayedTask(this, 2, false);
			return;
		}

		for (int x = (int) pos.x - 2; x < pos.x + 2; x++) {
			for (int y = (int) pos.y; y < pos.y + 4; y++) {
				for (int z = (int) pos.z - 2; z < pos.z + 2; z++) {
					pos.level.setBlockIdAt(x, y, z, Block.AIR);
				}
			}
		}

		for (int x = (int) pos.x - 2; x < pos.x + 2; x++) {
			for (int z = (int) pos.z - 2; z < pos.z + 2; z++) {
				pos.level.setBlockIdAt(x, pos.getFloorY(), z, Block.OBSIDIAN);
			}
		}

		if (player.hasEffect(Effect.ABSORPTION))
			player.removeEffect(Effect.ABSORPTION);

		player.teleport(pos.add(0, 1, 0));
		player.addExperience(1000);
	}
}
