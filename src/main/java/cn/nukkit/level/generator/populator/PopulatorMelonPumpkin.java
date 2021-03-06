package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;
import mobs.de.kniffo80.mobplugin.entities.utils.Utils;

public class PopulatorMelonPumpkin extends Populator {
    private ChunkManager level;
    private int randomAmount;
    private int baseAmount;

    public void setRandomAmount(int randomAmount) {
        this.randomAmount = randomAmount;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
    }

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
    	if (Utils.rand(0, 300) != 27)	{
    		return;
    	}
    	int block = Utils.rand() ? Block.MELON_BLOCK : Block.PUMPKIN;
        this.level = level;
        int amount = random.nextBoundedInt(this.randomAmount + 1) + this.baseAmount;
        for (int i = 0; i < amount; ++i) {
            int x = NukkitMath.randomRange(random, chunkX * 16, chunkX * 16 + 15);
            int z = NukkitMath.randomRange(random, chunkZ * 16, chunkZ * 16 + 15);
            int y = this.getHighestWorkableBlock(x, z);

            if (y != -1 && this.canTallGrassStay(x, y, z)) {
                this.level.setBlockIdAt(x, y, z, block);
            }
        }
    }

    private boolean canTallGrassStay(int x, int y, int z) {
        int b = this.level.getBlockIdAt(x, y, z);
        return (b == Block.AIR || b == Block.SNOW_LAYER) && this.level.getBlockIdAt(x, y - 1, z) == Block.GRASS && this.level.getBlockIdAt(x, y + 1, z) == Block.AIR;
    }

    private int getHighestWorkableBlock(int x, int z) {
        int y;
        for (y = 255; y >= 0; --y) {
            int b = this.level.getBlockIdAt(x, y, z);
            if (b != Block.AIR && b != Block.LEAVES && b != Block.LEAVES2 && b != Block.SNOW_LAYER) {
                break;
            }
        }

        return y == 0 ? -1 : ++y;
    }
}
