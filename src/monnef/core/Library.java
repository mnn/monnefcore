/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core;

import cpw.mods.fml.relauncher.ILibrarySet;

import java.util.ArrayList;
import java.util.HashMap;

import static monnef.core.MonnefCorePlugin.Log;

@Deprecated
public class Library implements ILibrarySet {

    public static final String DOWNLOAD_URL = Reference.URL_JAFFAS + "/lib/%s";

    private static final HashMap<String, LibraryInfo> Libraries;

    static {
        Libraries = new HashMap<String, LibraryInfo>();
        //addLibrary("Jsoup", "99351550d1fa2e8147319dbafd3f3a79d4f4c6e5", "jsoup-1.7.1.jar");
        //addLibrary("Lombok", "ba171d45e78f08ccca3cf531d285f13cfb4de2c7", "lombok_0_11_6.jar");
    }

    private static void addLibrary(String name, String hash, String fileName) {
        Libraries.put(name.toLowerCase(), new LibraryInfo(name, hash, fileName));
    }

    public Library() {
    }

    public static void tryDisableLibrary(String name) {
        if (Libraries.containsKey(name)) {
            Log.printInfo("Disabled download for library: " + name);
            Libraries.remove(name);
        } else {
            Log.printWarning("Not found library named \"" + name + "\", skipping.");
        }
    }

    @Override
    public String[] getLibraries() {
        return getLibraryInfo(EnumLibraryInfoType.FILE_NAME);
    }

    @Override
    public String[] getHashes() {
        return getLibraryInfo(EnumLibraryInfoType.HASH);
    }

    private String[] getLibraryInfo(EnumLibraryInfoType type) {
        ArrayList<String> tmp = new ArrayList<String>();
        for (LibraryInfo info : Libraries.values()) {
            String toAdd;

            switch (type) {
                case FILE_NAME:
                    toAdd = info.getFileName();
                    break;

                case HASH:
                    toAdd = info.getSha1Hash();
                    break;

                default:
                    throw new RuntimeException();
            }

            tmp.add(toAdd);
        }

        return tmp.toArray(new String[tmp.size()]);
    }

    @Override
    public String getRootURL() {
        return DOWNLOAD_URL;
    }

    private enum EnumLibraryInfoType {
        FILE_NAME, HASH
    }
}

