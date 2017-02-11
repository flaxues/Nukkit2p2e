package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockPackedIce extends BlockIce {

    public BlockPackedIce() {
        this(0);
    }

    public BlockPackedIce(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return PACKED_ICE;
    }

    @Override
    public String getName() {
        return "Packed Ice";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int onUpdate(int type) {
        return 0; //not being melted
    }

    @Override
    public int[][] getDrops(Item item) {
    	if (item.getEnchantment(Enchantment.ID_SILK_TOUCH) != null)	{
    		return new int[][] {
    			{
    				this.getId(),
    				0,
    				1
    			}
    		};
    	}
        return new int[0][0];
    }
}
