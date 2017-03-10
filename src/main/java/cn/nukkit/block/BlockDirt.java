package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import mobs.de.kniffo80.mobplugin.entities.utils.Utils;

/**
 * author: MagicDroidX Nukkit Project
 */
public class BlockDirt extends BlockSolid {

	public BlockDirt() {
		this(0);
	}

	public BlockDirt(int meta) {
		super(0);
	}

	@Override
	public int getId() {
		return DIRT;
	}

	@Override
	public boolean canBeActivated() {
		return true;
	}

	@Override
	public double getResistance() {
		return 2.5;
	}

	@Override
	public int[][] getDrops(Item item) {
		return new int[][] { { Item.DIRT, 0, 1 } };
	}

	@Override
	public double getHardness() {
		return 0.5;
	}

	@Override
	public int getToolType() {
		return ItemTool.TYPE_SHOVEL;
	}

	@Override
	public String getName() {
		return "Dirt";
	}

	@Override
	public boolean onActivate(Item item) {
		return this.onActivate(item, null);
	}

	@Override
	public boolean onActivate(Item item, Player player) {
		if (item.isHoe()) {
			item.useOn(this);
			this.getLevel().setBlock(this, new BlockFarmland(), true);

			return true;
		}

		return false;
	}

	@Override
	public BlockColor getColor() {
		return BlockColor.DIRT_BLOCK_COLOR;
	}

	@Override
	public int onUpdate(int type) {
		if (type == Level.BLOCK_UPDATE_RANDOM) {
			if (Utils.rand()) {
				Block block = null;
				switch (Utils.rand(0, 12)) {
				case 0:
					block = this.getSide(Vector3.SIDE_NORTH);
					break;
				case 1:
					block = this.getSide(Vector3.SIDE_EAST);
					break;
				case 2:
					block = this.getSide(Vector3.SIDE_SOUTH);
					break;
				case 3:
					block = this.getSide(Vector3.SIDE_WEST);
					break;
				case 4:
					block = this.getSide(Vector3.SIDE_UP).getSide(Vector3.SIDE_NORTH);
					break;
				case 5:
					block = this.getSide(Vector3.SIDE_UP).getSide(Vector3.SIDE_EAST);
					break;
				case 6:
					block = this.getSide(Vector3.SIDE_UP).getSide(Vector3.SIDE_SOUTH);
					break;
				case 7:
					block = this.getSide(Vector3.SIDE_UP).getSide(Vector3.SIDE_WEST);
					break;
				case 8:
					block = this.getSide(Vector3.SIDE_DOWN).getSide(Vector3.SIDE_NORTH);
					break;
				case 9:
					block = this.getSide(Vector3.SIDE_DOWN).getSide(Vector3.SIDE_EAST);
					break;
				case 10:
					block = this.getSide(Vector3.SIDE_DOWN).getSide(Vector3.SIDE_SOUTH);
					break;
				case 11:
					block = this.getSide(Vector3.SIDE_DOWN).getSide(Vector3.SIDE_WEST);
					break;
				}

				if (block == null) {
					Server.getInstance().getLogger().alert("A block was NULL!!! @ cn.nukkit.block.BlockDirt.java:131");
				} else {
					this.level.setBlock(this, new BlockGrass(), false, true);
				}
			}
		}
		return 0;
	}
}
