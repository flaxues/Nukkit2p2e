package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityPortalEnterEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.BlockColor;
import tk.daporkchop.task.GenEnderPortalTask;

public class BlockEndPortal extends BlockFlowable {

    public BlockEndPortal() {
        this(0);
    }

    public BlockEndPortal(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "End Portal Block";
    }

    @Override
    public int getId() {
        return END_PORTAL;
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
    public double getResistance() {
        return 18000000;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
    	
    	Level world = Server.getInstance().getDefaultLevel();
        
        EntityPortalEnterEvent ev = new EntityPortalEnterEvent(entity, EntityPortalEnterEvent.TYPE_END);
        this.level.getServer().getPluginManager().callEvent(ev);
        
        if (ev.isCancelled()) {
            return;
        }
        
        // PortalPort!
     	Level end = Server.getInstance().getEndLevel();
     	
         if (this.getLevel() == world)  {
     	    Position pos = new Position(0, 64, 0, end);
     	    if (entity instanceof Player)	{
     		    genPortal(pos, (Player) entity);
     	    }
         	entity.teleport(pos);
        } else {
        	if (entity instanceof Player)  {
        	    entity.teleport(((Player) entity).spawnPosition == null ? new Position(0, 256, 0, world) : ((Player) entity).spawnPosition);
            } else {
            	entity.teleport(new Position(0, 256, 0, world));
            } 
        } 
    }


    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
    
    @Override
	public boolean onBreak(Item item) {
		boolean result = super.onBreak(item);
		for (int side = 0; side <= 5; side++) {
			Block b = this.getSide(side);
			if (b != null) {
				if (b instanceof BlockEndPortal) {
					result &= b.onBreak(item);
				}
			}
		}
		return result;
	}
    
    public void genPortal(Position pos, Player p) {		
		Server.getInstance().getScheduler().scheduleDelayedTask(new GenEnderPortalTask(pos, p), 2, false);
	}
}
