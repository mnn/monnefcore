/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.api;

import net.minecraft.util.IIcon;

public interface ICustomIcon {
    String getDefaultModName();

    String getModName();

    void setModName(String newModName);

    int getDefaultSheetNumber();

    void setCustomIconIndex(int index);

    int getCustomIconIndex();

    void setSheetNumber(int index);

    int getSheetNumber();

    String getCustomIconName();

    int getIconsCount();

    void setIconsCount(int iconsCount);

    IIcon getCustomIcon(int index);
}
