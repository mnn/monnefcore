/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.test

import org.junit._
import Assert._
import monnef.core.utils.ItemStackList
import net.minecraft.item.ItemStack

class ItemStackListTest {

  @Test def blank() = {
    val n = "Name ^_^ "
    val s = new ItemStackList(n)
    assertEquals(n, s.name)
    assertFalse(s.contains(new ItemStack(1, 1, 0)))
  }

  @Ignore def contains() {
    val l = new ItemStackList("x")
    assertFalse(l.contains(new ItemStack(10, 0, 0)))
    assertFalse(l.contains(new ItemStack(11, 0, 0)))
    assertFalse(l.contains(new ItemStack(12, 0, 0)))

    l.add(new ItemStack(10, 0, 0))
    assertTrue(l.contains(new ItemStack(10, 0, 0)))
    assertFalse(l.contains(new ItemStack(10, 0, 1)))

    l.add(11)
    assertTrue(l.contains(new ItemStack(11, 0, 0)))
    assertTrue(l.contains(new ItemStack(11, 0, 1)))

    l.clear()
    assertTrue(l.size == 0)

    l.addWithTranslatedMeta(12)
    assertTrue(l.contains(new ItemStack(12, 0, 0)))
    assertTrue(l.contains(new ItemStack(12, 0, 1)))

    l.add(13, 5)
    assertTrue(l.contains(new ItemStack(13, 0, 5)))
    assertFalse(l.contains(new ItemStack(13, 0, 6)))
  }
}
