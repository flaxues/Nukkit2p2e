package tk.daporkchop.task;

import java.util.TimerTask;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

public class GenPortalTask extends TimerTask {
	
	public Position pos;
	public Player player;
	
	public GenPortalTask(Position pos, Player player)	{
		this.pos = pos;
		this.player = player;
	}
	
	public GenPortalTask(int x, int y, int z, Level lvl, Player player)	{
		this.pos = new Position(x, y, z, lvl);
		this.player = player;
	}
	
	@Override
	public void run()	{
		player.sendMessage("&lHere, have the crappiest return portal ever made.");
		player.sendMessage("&lIt's not the best, but it seems to prevent causing the server to crash.");
		player.sendMessage("&lThe portal is directly below you.");
		player.sendTip("&l&cThe portal is directly below you.");
		
		for (int x = (int) (pos.x - 3); x < pos.x + 4; x++)	{
			for (int y = (int) pos.y; y < pos.y + 3; y++)	{
				for (int z = (int) (pos.z - 3); z < pos.z + 4; z++)	{
					pos.level.setBlockIdAt(x, y, z, Block.AIR);
				}
			}
		}
		
		pos.level.setBlockIdAt(pos.x, pos.y - 2, pos.z, Block.NETHER_PORTAL);
		pos.level.setBlockIdAt(pos.x, pos.y - 1, pos.z, Block.OBSIDIAN);
		pos.level.setBlockIdAt(pos.x, pos.y, pos.z, Block.AIR);
		pos.level.setBlockIdAt(pos.x, pos.y + 1, pos.z, Block.AIR);
		pos.level.setBlockIdAt(pos.x, pos.y + 2, pos.z, Block.OBSIDIAN);
	}
}
