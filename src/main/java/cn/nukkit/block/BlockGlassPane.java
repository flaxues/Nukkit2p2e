package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.BlockColor;

/**
 * Created on 2015/12/6 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockGlassPane extends BlockThin {

    public BlockGlassPane() {
        this(0);
    }

    public BlockGlassPane(int meta) {
        super(0);
    }


    @Override
    public String getName() {
        return "Glass Pane";
    }

    @Override
    public int getId() {
        return GLASS_PANE;
    }

    @Override
    public double getResistance() {
        return 1.5;
    }

    @Override
    public double getHardness() {
        return 0.3;
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

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}
