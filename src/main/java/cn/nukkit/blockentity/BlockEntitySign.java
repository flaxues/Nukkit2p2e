package cn.nukkit.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class BlockEntitySign extends BlockEntitySpawnable {

    public BlockEntitySign(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        if (!nbt.contains("Text1")) {
            nbt.putString("Text1", "");
        }
        if (!nbt.contains("Text2")) {
            nbt.putString("Text2", "");
        }
        if (!nbt.contains("Text3")) {
            nbt.putString("Text3", "");
        }
        if (!nbt.contains("Text4")) {
            nbt.putString("Text4", "");
        }
        this.namedTag = nbt;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.remove("Creator");
    }

    @Override
    public boolean isBlockEntityValid() {
        int blockID = getBlock().getId();
        return blockID == Block.SIGN_POST || blockID == Block.WALL_SIGN;
    }

    /**
     * Sets the line of text at the specified index.
     * 
     * For example, setLine(0, "Line One") will set the first line of text to "Line One".
     * 
     * @param index  Line number to set the text at, starting from 0
     * @param line  New text to set at the specified index
     * @throws IndexOutOfBoundsException If the index is out of the range 0..3
     */
    public void setText(int index, String line) {
        if (0 > index || index > 3) throw new IndexOutOfBoundsException();
        String line1 = (index == 0 ? line : getText()[0]);
        String line2 = (index == 1 ? line : getText()[1]);
        String line3 = (index == 2 ? line : getText()[2]);
        String line4 = (index == 3 ? line : getText()[3]);
        this.setText(line1, line2, line3, line4);
    }
    
    public boolean setText() {
        return this.setText("");
    }

    public boolean setText(String line1) {
        return this.setText(line1, "");
    }

    public boolean setText(String line1, String line2) {
        return this.setText(line1, line2, "");
    }

    public boolean setText(String line1, String line2, String line3) {
        return this.setText(line1, line2, line3, "");
    }

    public boolean setText(String line1, String line2, String line3, String line4) {
        this.namedTag.putString("Text1", line1);
        this.namedTag.putString("Text2", line2);
        this.namedTag.putString("Text3", line3);
        this.namedTag.putString("Text4", line4);
        this.spawnToAll();

        if (this.chunk != null) {
            this.chunk.setChanged();
            this.level.clearChunkCache(this.chunk.getX(), this.chunk.getZ());
        }

        return true;
    }

    public String[] getText() {
        return new String[]{
                this.namedTag.getString("Text1"),
                this.namedTag.getString("Text2"),
                this.namedTag.getString("Text3"),
                this.namedTag.getString("Text4")
        };
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag()
                .putString("id", BlockEntity.SIGN)
                .putString("Text1", this.namedTag.getString("Text1"))
                .putString("Text2", this.namedTag.getString("Text2"))
                .putString("Text3", this.namedTag.getString("Text3"))
                .putString("Text4", this.namedTag.getString("Text4"))
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z);

    }

}
