package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockGlowstone;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.object.ore.ObjectOre;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.math.NukkitRandom;

public class PopulatorGlowStone extends Populator {

	private OreType type = new OreType(new BlockGlowstone(), 1, 20, 10, 128);

	@Override
	public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
		BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);
		int bx = chunkX << 4;
		int bz = chunkZ << 4;
		ObjectOre ore = new ObjectOre(random, type, Block.AIR);
		for (int i = 0; i < ore.type.clusterCount; ++i) {
			int x = random.nextRange(0, 15);
			int z = random.nextRange(0, 15);
			int y = this.getHighestWorkableBlock(chunk, x, z);
			if (y != -1) {
				ore.placeObject(level, bx + x, y, bz + z);
			}
		}
	}

	private int getHighestWorkableBlock(FullChunk chunk, int x, int z) {
		int y;
		for (y = 127; y >= 0; y--) {
			int b = chunk.getBlockId(x, y, z);
			if (b == Block.AIR) {
				break;
			}
		}
		return y == 0 ? -1 : y;
	}
}
