/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.test

import org.junit._
import org.junit.Assert._
import monnef.core.utils.ItemStackList
import net.minecraft.item.ItemStack
import net.minecraft.init.Items

class ItemStackListTest {

  def i1 = Items.apple

  def i2 = Items.beef

  def i3 = Items.cake

  def i4 = Items.diamond

  @Test def blank() = {
    val n = "Name ^_^ "
    val s = new ItemStackList(n)
    assertEquals(n, s.name)
    assertFalse(s.contains(new ItemStack(i1, 1, 0)))
  }

  @Ignore def contains() {
    val l = new ItemStackList("x")
    assertFalse(l.contains(new ItemStack(i1, 0, 0)))
    assertFalse(l.contains(new ItemStack(i2, 0, 0)))
    assertFalse(l.contains(new ItemStack(i3, 0, 0)))

    l.add(new ItemStack(i1, 0, 0))
    assertTrue(l.contains(new ItemStack(i1, 0, 0)))
    assertFalse(l.contains(new ItemStack(i1, 0, 1)))

    l.add(i2)
    assertTrue(l.contains(new ItemStack(i2, 0, 0)))
    assertTrue(l.contains(new ItemStack(i2, 0, 1)))

    l.clear()
    assertTrue(l.size == 0)

    l.addWithTranslatedMeta(i3)
    assertTrue(l.contains(new ItemStack(i3, 0, 0)))
    assertTrue(l.contains(new ItemStack(i3, 0, 1)))

    l.add(i4, 5)
    assertTrue(l.contains(new ItemStack(i4, 0, 5)))
    assertFalse(l.contains(new ItemStack(i4, 0, 6)))
  }
}
