package cn.nukkit.block;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityPortalEnterEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.BlockColor;

/**
 * Created on 2016/1/5 by xtypr. Package cn.nukkit.block in project nukkit . The name NetherPortalBlock comes from minecraft wiki.
 */
public class BlockNetherPortal extends BlockFlowable {

	public byte[][] portalBlocks = new byte[][] { {
				0, 0, 0, 0, 0, 0,
				0, 0, 1, 1, 0, 0,
				0, 1, 1, 1, 1, 0,
				0, 0, 1, 1, 0, 0,
				0, 0, 0, 0, 0, 0
			},
			{
				0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0,
				0, 1, 2, 2, 1, 0,
				0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0
			},
			{
				0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0,
				0, 1, 2, 2, 1, 0,
				0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0
			},
			{
				0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0,
				0, 1, 2, 2, 1, 0,
				0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0
			},
			{
				0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0,
				0, 1, 2, 2, 1, 0,
				0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0
			},
			{
				0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0,
				0, 1, 1, 1, 1, 0,
				0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0
			}
		};

	public BlockNetherPortal() {
		this(0);
	}

	public BlockNetherPortal(int meta) {
		super(0);
	}

	@Override
	public String getName() {
		return "Nether Portal Block";
	}

	@Override
	public int getId() {
		return NETHER_PORTAL;
	}

	@Override
	public boolean canPassThrough() {
		return true;
	}

	@Override
	public boolean isBreakable(Item item) {
		return false;
	}

	@Override
	public double getHardness() {
		return -1;
	}

	@Override
	public int getLightLevel() {
		return 11;
	}

	@Override
	public boolean onBreak(Item item) {
		boolean result = super.onBreak(item);
		for (int side = 0; side <= 5; side++) {
			Block b = this.getSide(side);
			if (b != null) {
				if (b instanceof BlockNetherPortal) {
					result &= b.onBreak(item);
				}
			}
		}
		return result;
	}

	@Override
	public boolean hasEntityCollision() {
		return true;
	}

	@Override
	public void onEntityCollide(Entity entity) {
		entity.inPortalTicks++;

		if (entity.inPortalTicks >= 80) {
			entity.inPortalTicks = 0;
			EntityPortalEnterEvent ev = new EntityPortalEnterEvent(entity, EntityPortalEnterEvent.TYPE_NETHER);
			this.level.getServer().getPluginManager().callEvent(ev);

			if (ev.isCancelled()) {
				return;
			}
			// PortalPort!
			Level nether = Server.getInstance().getLevelByName("nether"), world = Server.getInstance().getLevelByName("world");

			boolean toNether = entity.level == world; // true if entity needs to go to nether, false otherwise

			if (toNether) {
				int x = entity.chunk.getX() * 16 * 8, z = entity.chunk.getZ() * 16 * 8;
				Position pos = new Position(x, entity.level.getHighestBlockAt(x, z), z, world);
				entity.teleport(pos);
			} else {
				int x = entity.chunk.getX() * 16 / 8, z = entity.chunk.getZ() * 16 / 8;
				Position pos = new Position(x, entity.level.getHighestBlockAt(x, z), z, world);
				entity.teleport(pos);
			}
		}
	}

	@Override
	public BlockColor getColor() {
		return BlockColor.AIR_BLOCK_COLOR;
	}

	public boolean checkForPortal(Level lvl, Position pos) {
		if (lvl.getBlockIdAt(pos.getFloorX(), pos.getFloorY(), pos.getFloorZ()) != Block.OBSIDIAN) {
			return false;
		}

		return true;
	}

	public void genPortal(Level lvl, Position pos) {		
		for (int i = 0; i < portalBlocks.length; i++)	{
			for (int j = 0; j < 5; j++)	{
				for (int k = 0; k < 6; k++)	{
					switch (portalBlocks[i][(j * 5 + k)])	{
					case 0:
						lvl.setBlock(new Position(pos.x + j, pos.y + i, pos.z + j * 5 + k), new BlockAir());
						break;
					case 1:
						lvl.setBlock(new Position(pos.x + j, pos.y + i, pos.z + j * 5 + k), new BlockObsidian());
						break;
					case 2:
						lvl.setBlock(new Position(pos.x + j, pos.y + i, pos.z + j * 5 + k), new BlockNetherPortal());
						break;
					default:
						break;
					}
				}
			}
		}
	}
}
