/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.item;

import monnef.core.MonnefCorePlugin;
import monnef.core.api.ICustomIcon;
import monnef.core.common.CustomIconHelper;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;

import java.util.List;

public abstract class ItemMonnefCore extends Item implements ICustomIcon {
    public static final String BETA_WARNING_TEXT = "\u00A7lnot finished!\u00A7r";
    protected int customIconIndex;
    protected int sheetNumber;
    protected int iconsCount = 1;
    protected Icon[] icons;

    public ItemMonnefCore(int id) {
        super(id);
        this.sheetNumber = getDefaultSheetNumber();
    }

    @Override
    public void setCustomIconIndex(int index) {
        this.customIconIndex = index;
    }

    @Override
    public int getSheetNumber() {
        return sheetNumber;
    }

    @Override
    public int getCustomIconIndex() {
        return customIconIndex;
    }

    @Override
    public void setSheetNumber(int index) {
        this.sheetNumber = index;
    }

    @Override
    public String getCustomIconName() {
        return null;
    }

    @Override
    public int getIconsCount() {
        return iconsCount;
    }

    @Override
    public void setIconsCount(int iconsCount) {
        this.iconsCount = iconsCount;
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(CustomIconHelper.generateId(this));
        if (iconsCount > 1) {
            icons = new Icon[iconsCount];
            icons[0] = this.itemIcon;
            for (int i = 1; i < iconsCount; i++) {
                icons[i] = iconRegister.registerIcon(CustomIconHelper.generateShiftedId(this, i));
            }
        }
    }

    @Override
    public Icon getCustomIcon(int index) {
        return icons[index];
    }

    public static void initNBT(ItemStack stack) {
        if (stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
    }

    public void removeFromCreativeTab() {
        if (!MonnefCorePlugin.debugEnv) setCreativeTab(null);
    }

    private boolean inBetaStage;

    public void markAsBeta() {
        inBetaStage = true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List result, boolean par4) {
        super.addInformation(stack, player, result, par4);
        if (inBetaStage) {
            result.add(BETA_WARNING_TEXT);
        }
    }
}
