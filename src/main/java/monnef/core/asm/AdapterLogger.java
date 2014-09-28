package monnef.core.asm;

import monnef.core.MonnefCorePlugin;

import java.util.ArrayList;
import java.util.LinkedList;

public class AdapterLogger {
    private LinkedList<String> messages = new LinkedList<String>();
    private String prefix;

    public AdapterLogger(String prefix) {
        this.prefix = prefix;
    }

    public void log(String msg) {
        messages.add(msg);
    }

    public void printAll() {
        printInternal("List of all adapter logging info");
        printInternal("--------------------------------");
        for (String msg : messages) {
            printInternal(msg);
        }
    }

    private void printInternal(String msg) {
        print("[logger] " + msg);
    }

    public void print(String msg) {
        MonnefCorePlugin.Log.printInfo(prefix + msg);
    }

    public void printError(String msg) {
        MonnefCorePlugin.Log.printSevere(prefix + msg);
    }

    public static String getVarsForLog(ArrayList<Integer> localVars) {
        StringBuilder sb = new StringBuilder();
        for (Integer var : localVars) {
            sb.append(var);
            sb.append(";");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
