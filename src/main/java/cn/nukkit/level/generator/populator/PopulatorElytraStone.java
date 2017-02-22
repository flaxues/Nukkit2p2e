package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;
import mobs.milk.pureentities.util.Utils;

public class PopulatorElytraStone extends Populator {

	@Override
	public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
		if (Utils.rand(0, 200) == 127) {
			int x = chunkX * 16 + Utils.rand(0, 15);
			int z = chunkZ * 16 + Utils.rand(0, 15);
			int y;
			for (y = 255; y > 1; y--)	{
				if (level.getBlockIdAt(x, y, z) != Block.END_STONE)	{
					continue;
				}
				
				level.setBlockIdAt(x, y + 1, z, Block.SEA_LANTERN);
				break;
			}
		}
	}
}
