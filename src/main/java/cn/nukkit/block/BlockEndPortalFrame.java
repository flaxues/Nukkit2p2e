package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;

/**
 * Created by Pub4Game on 26.12.2015.
 */
public class BlockEndPortalFrame extends BlockTransparent {

    public BlockEndPortalFrame() {
        this(0);
    }

    public BlockEndPortalFrame(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return END_PORTAL_FRAME;
    }

    @Override
    public double getResistance() {
        return 18000000;
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public int getLightLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "End Portal Frame";
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(
                x,
                y,
                z,
                x + 1,
                y + ((this.getDamage() & 0x04) > 0 ? 1 : 0.8125),
                z + 1
        );
    }
    
    @Override
    public boolean onBreak(Item item) {
        //destroy the end portal
        Block[] nearby = new Block[]{
                this.getSide(Vector3.SIDE_NORTH), this.getSide(Vector3.SIDE_SOUTH),
                this.getSide(Vector3.SIDE_WEST), this.getSide(Vector3.SIDE_EAST),
        };
        for (Block aNearby : nearby) {
            if (aNearby != null) if (aNearby.getId() == END_PORTAL) {
                aNearby.onBreak(item);
            }
        }
        return super.onBreak(item);
    }
}
