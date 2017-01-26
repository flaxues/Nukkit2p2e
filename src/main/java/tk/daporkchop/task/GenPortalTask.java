package tk.daporkchop.task;

import java.util.TimerTask;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.potion.Effect;

public class GenPortalTask extends TimerTask {

	public Position pos;
	public Player player;
	public int count = 0;
	public Server s;

	public GenPortalTask(Position pos, Player player) {
		this.pos = pos;
		this.player = player;
		Effect effect = Effect.getEffect(Effect.ABSORPTION);
		effect.setAmbient(false);
		effect.setDuration(Integer.MAX_VALUE);
		effect.setAmplifier(127);
		player.addEffect(effect);
		this.s = Server.getInstance();
	}

	public GenPortalTask(int x, int y, int z, Level lvl, Player player) {
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

		for (int x = (int) (pos.x - 3); x < pos.x + 4; x++) {
			for (int y = (int) pos.y; y < pos.y + 3; y++) {
				for (int z = (int) (pos.z - 3); z < pos.z + 4; z++) {
					pos.level.setBlockIdAt(x, y, z, Block.AIR);
				}
			}
		}

		pos.level.setBlockIdAt(pos.x, pos.y, pos.z, Block.NETHER_PORTAL);
		pos.level.setBlockIdAt(pos.x, pos.y + 1, pos.z, Block.NETHER_PORTAL);
		pos.level.setBlockIdAt(pos.x, pos.y + 2, pos.z, Block.NETHER_PORTAL);
		pos.level.setBlockIdAt(pos.x + 1, pos.y, pos.z, Block.NETHER_PORTAL);
		pos.level.setBlockIdAt(pos.x + 1, pos.y + 1, pos.z, Block.NETHER_PORTAL);
		pos.level.setBlockIdAt(pos.x + 1, pos.y + 2, pos.z, Block.NETHER_PORTAL);
		//This should generate the portal blocks.

		pos.level.setBlockIdAt(pos.x, pos.y - 1, pos.z, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x + 1, pos.y - 1, pos.z, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x + 1, pos.y - 1, pos.z - 1, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x + 1, pos.y - 1, pos.z + 1, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x, pos.y - 1, pos.z - 1, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x, pos.y - 1, pos.z + 1, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x - 1, pos.y - 1, pos.z, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x + 2, pos.y - 1, pos.z, Block.OBSIDIAN);
		//This should generate the base of the portal

		pos.level.setBlockIdAt(pos.x - 1, pos.y, pos.z, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x - 1, pos.y + 1, pos.z, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x - 1, pos.y + 2, pos.z, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x - 1, pos.y + 3, pos.z, Block.OBSIDIAN);
		//This should generate one side of the portal

		pos.level.setBlockIdAt(pos.x + 2, pos.y, pos.z, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x + 2, pos.y + 1, pos.z, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x + 2, pos.y + 2, pos.z, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x + 2, pos.y + 3, pos.z, Block.OBSIDIAN);
		//This should generate the other side of the portal

		pos.level.setBlockIdAt(pos.x + 1, pos.y + 3, pos.z, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x, pos.y + 3, pos.z, Block.OBSIDIAN);
		//This should generate the top of the portal

		if (player.hasEffect(Effect.ABSORPTION))
			player.removeEffect(Effect.ABSORPTION);

		pos.x++;
		pos.y++;
		//pos.z++;

		player.teleport(pos);
	}
}
