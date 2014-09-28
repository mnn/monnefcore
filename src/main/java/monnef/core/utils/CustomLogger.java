/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import cpw.mods.fml.common.FMLLog;
import monnef.core.MonnefCorePlugin;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CustomLogger {
    private Logger logger;

    public CustomLogger(String logTitle) {
        Logger parent = FMLLog.getLogger();
        logger = LogManager.getLogger(logTitle);

        /*
        logger.setParent(parent);
        logger.setUseParentHandlers(true);
        */
    }

    public void printInfo(String message) {
        print(Level.INFO, message);
    }

    public void printWarning(String message) {
        print(Level.WARN, message);
    }

    public void printSevere(String message) {
        print(Level.FATAL, message);
    }

    public void print(Level level, String message) {
        logger.log(level, message);
    }

    public void printDebug(String message) {
        if (MonnefCorePlugin.debugEnv) {
            print(Level.INFO, "[D] " + message);
        }
    }

    public void printFine(String message) {
        print(Level.TRACE, message);
    }

    public void printFinest(String message) {
        print(Level.TRACE, message);
    }
}
