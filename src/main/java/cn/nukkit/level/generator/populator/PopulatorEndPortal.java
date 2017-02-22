package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;
import mobs.milk.pureentities.util.Utils;

public class PopulatorEndPortal extends Populator {

	@Override
	public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
		if (Utils.rand(0, 5000) == 2048) {
			int x = chunkX * 16;
			int z = chunkZ * 16;
			int y = 20;

			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					for (int k = 0; k < 8; k++) {
						level.setBlockIdAt(x + i, y + k, z + j, ((i == 0 || i == 15) || (j == 0 || j == 15) || (k == 0 || k == 7)) ? Block.STONE_BRICKS : Block.AIR);
					}
				}
			}
			y += 2;
			level.setBlockIdAt(x + 5, y, z + 6, Block.END_PORTAL_FRAME);
			level.setBlockIdAt(x + 5, y, z + 7, Block.END_PORTAL_FRAME);
			level.setBlockIdAt(x + 5, y, z + 8, Block.END_PORTAL_FRAME);
			//First side of the portal

			level.setBlockIdAt(x + 6, y, z + 5, Block.END_PORTAL_FRAME);
			level.setBlockIdAt(x + 7, y, z + 5, Block.END_PORTAL_FRAME);
			level.setBlockIdAt(x + 8, y, z + 5, Block.END_PORTAL_FRAME);
			//Second side of the portal

			level.setBlockIdAt(x + 9, y, z + 6, Block.END_PORTAL_FRAME);
			level.setBlockIdAt(x + 9, y, z + 7, Block.END_PORTAL_FRAME);
			level.setBlockIdAt(x + 9, y, z + 8, Block.END_PORTAL_FRAME);
			//Third side of the portal

			level.setBlockIdAt(x + 6, y, z + 9, Block.END_PORTAL_FRAME);
			level.setBlockIdAt(x + 7, y, z + 9, Block.END_PORTAL_FRAME);
			level.setBlockIdAt(x + 8, y, z + 9, Block.END_PORTAL_FRAME);
			//Fourth side of the portal

			level.setBlockIdAt(x + 7, y, z + 7, Block.END_PORTAL);
			level.setBlockIdAt(x + 7, y, z + 8, Block.END_PORTAL);
			level.setBlockIdAt(x + 7, y, z + 9, Block.END_PORTAL);
			level.setBlockIdAt(x + 8, y, z + 7, Block.END_PORTAL);
			level.setBlockIdAt(x + 8, y, z + 8, Block.END_PORTAL);
			level.setBlockIdAt(x + 8, y, z + 9, Block.END_PORTAL);
			level.setBlockIdAt(x + 9, y, z + 7, Block.END_PORTAL);
			level.setBlockIdAt(x + 9, y, z + 8, Block.END_PORTAL);
			level.setBlockIdAt(x + 9, y, z + 9, Block.END_PORTAL);
			//Portal blocks

			y--;
			for (int i = 7; i < 10; i++) {
				for (int j = 7; j < 10; j++) {
					level.setBlockIdAt(x + i, y, z + j, Block.INVISIBLE_BEDROCK);
				}
			}
		}
	}
}
