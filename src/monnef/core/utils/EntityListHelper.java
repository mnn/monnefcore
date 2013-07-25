/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.entity.EntityList;

public class EntityListHelper {
    public static boolean idExists(int id) {
        return EntityList.IDtoClassMapping.containsKey(id);
    }
}
