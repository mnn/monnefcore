/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class BookWriter {
    private static final String NEW_LINE = "\n";
    private StringBuilder pageBuffer = new StringBuilder();
    private ItemStack book;
    private NBTTagList pages;
    private int pageNum = 0;
    private int lines = 0;
    private NBTTagCompound tag;

    public BookWriter(String author, String title) {
        book = new ItemStack(Item.writtenBook);
        tag = new NBTTagCompound();

        tag.setString("title", title);
        tag.setString("author", author);

        pages = new NBTTagList();
    }

    public void endPage() {
        pages.appendTag(new NBTTagString("page" + (++pageNum), pageBuffer.toString()));
        pageBuffer.setLength(0);
        lines = 0;
    }

    public void addLine(String line) {
        pageBuffer.append(line);
        pageBuffer.append(NEW_LINE);
        lines++;
        if (lines > 14) endPage();
    }

    public ItemStack finish() {
        tag.setTag("pages", pages);

        book.setTagCompound(tag);
        ItemStack tmp = book;
        book = null; // trash finished book, don't allow further manipulation with it
        return tmp;
    }

    public void addBlankLine() {
        addLine("");
    }
}
