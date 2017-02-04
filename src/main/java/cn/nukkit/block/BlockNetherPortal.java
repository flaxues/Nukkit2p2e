package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityPortalEnterEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.BlockColor;
import tk.daporkchop.task.GenPortalTask;

/**
 * Created on 2016/1/5 by xtypr. Package cn.nukkit.block in project nukkit . The name NetherPortalBlock comes from minecraft wiki.
 */
public class BlockNetherPortal extends BlockFlowable {

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
		
		if (entity instanceof Player && ((Player) entity).hasPortaled)	{
			return;
		}
		
		entity.inPortalTicks++;

		if (entity.inPortalTicks == 160) {
			entity.inPortalTicks = 0;
			EntityPortalEnterEvent ev = new EntityPortalEnterEvent(entity, EntityPortalEnterEvent.TYPE_NETHER);
			this.level.getServer().getPluginManager().callEvent(ev);

			if (ev.isCancelled()) {
				return;
			}
			// PortalPort!
			Level nether = Server.getInstance().getNetherLevel(), world = Server.getInstance().getDefaultLevel();

			boolean toNether = entity.level == world; // true if entity needs to go to nether, false otherwise

			if (toNether) {
				int x = entity.chunk.getX() * 16 / 8, z = entity.chunk.getZ() * 16 / 8;
				Position pos = new Position(x, 64, z, nether);
				if (entity instanceof Player)	{
					genPortal(pos, (Player) entity);
				}
				entity.teleport(pos);
			} else {
				int x = entity.chunk.getX() * 16 * 8, z = entity.chunk.getZ() * 16 * 8;
				Position pos = new Position(x, 64, z, world);
				if (entity instanceof Player)	{
					genPortal(pos, (Player) entity);
				}
				entity.teleport(pos);
			}
		}
	}

	@Override
	public BlockColor getColor() {
		return BlockColor.AIR_BLOCK_COLOR;
	}

	public void genPortal(Position pos, Player p) {	
		p.hasPortaled = true;
		Server.getInstance().getScheduler().scheduleDelayedTask(new GenPortalTask(pos, p), 2, false);
	}
}
