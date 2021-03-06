/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.asm;

import monnef.core.Reference;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static monnef.core.MonnefCorePlugin.Log;

public class PathHelper {
    public static String getMyPath() {
        return getMyPath(Reference.class);
    }

    public static String getMyPath(Class classToLocate) {
        File file = getMyPathFile(classToLocate);

        String absPath = file.getAbsolutePath();
        Log.printFine("my jar's absolute path: [" + absPath + "], detected from class: [" + classToLocate.getSimpleName() + "]");
        return absPath;
    }

    private static File getCorePathFile() {
        return getMyPathFile(Reference.class);
    }

    private static File getMyPathFile(Class classToLocate) {
        URL url = classToLocate.getProtectionDomain().getCodeSource().getLocation();
        File file;
        URI uri = null;
        try {
            uri = url.toURI();
            String path = uri.toString();

            if (path.startsWith("jar")) {
                // [jar:file:/D:/games/Minecraft/instances/mc%201.4.7/minecraft/coremods/mod_monnef_core_0.4.12.jar!/monnef/core/Library.class]
                Pattern patty = Pattern.compile("^(?:jar:file:)*(/.*)!/.*?$");

                Matcher match = patty.matcher(path);
                if (match.find()) {
                    path = match.group(1);
                } else {
                    throw new RuntimeException("cannot parse path to my jar");
                }
            } else {
                throw new RuntimeException("what? I'm not in a jar?");
            }

            Log.printFine("my jar's path: [" + path + "]");
            file = new File(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("cannot detect my path");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("cannot detect my path, uri: [" + (uri == null ? "NULL" : uri.toString()) + "]");
        }
        return file;
    }

    public static boolean createDirIfNecessary(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            return dir.mkdir();
        } else {
            return dir.isDirectory();
        }
    }
}
