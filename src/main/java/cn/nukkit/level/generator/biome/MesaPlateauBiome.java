package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockClayHardened;
import cn.nukkit.block.BlockClayStained;
import cn.nukkit.level.generator.populator.PopulatorDeadBush;

public class MesaPlateauBiome extends NormalBiome {

	public MesaPlateauBiome() {
		
		this.setGroundCover(new Block[] {
				new BlockClayStained(1),
				new BlockClayStained(14),
				new BlockClayStained(4),
				new BlockClayHardened(),
				new BlockClayStained(14),
				new BlockClayHardened(),
				new BlockClayStained(8),
				new BlockClayStained(4),
				new BlockClayStained(12),
				new BlockClayStained(12),
				new BlockClayHardened(),
				new BlockClayStained(0),
				new BlockClayHardened(),
		});
		
		PopulatorDeadBush deadBush = new PopulatorDeadBush();
		deadBush.setBaseAmount(4);
		deadBush.setRandomAmount(4);
		this.addPopulator(deadBush);
		
		this.temperature = 2;
        this.rainfall = 0;
		this.setElevation(90, 93);
	}

	@Override
	public String getName() {
		return "Mesa Plateau";
	}

}
