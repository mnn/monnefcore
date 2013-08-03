/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.collection;

import java.util.Map;

public interface IMapFactory<I extends Map> {
    I build();
}
