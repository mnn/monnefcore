package monnef.core.asm;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import monnef.core.MonnefCorePlugin;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.common.base.Charsets;
import monnef.core.utils.CustomLogger;

// heavily based on SevenCommons from diesieben07
public class SrgNames {
    private static final Map<String, String> fields;
    private static final Map<String, String> methods;

    private static final String SYS_PROP_NAME = "monnefCore.mappingsFile";

    public static final SrgMapping C_WORLD_SERVER = new SrgMappingClass("net.minecraft.world.WorldServer");
    public static final SrgMapping C_WORLD = new SrgMappingClass("net.minecraft.world.World");
    public static final SrgMapping M_TICK_BLOCKS_AND_AMBIANCE = new SrgMappingMethod("func_147456_g");
    public static final SrgMapping M_CAN_LIGHTNING_STRIKE_AT = new SrgMappingMethod("func_72951_B");
    public static final SrgMapping C_ITEM = new SrgMappingClass("net.minecraft.item.Item");

    private SrgNames() {
    }

    public static abstract class SrgMapping {
        public final String innerName;

        public SrgMapping(String name) {
            this.innerName = name;
        }

        public boolean isEqualName(String other) {
            String name = getTranslatedName();
            if (name == null) {
                return innerName.equals(other);

                /*
                dump();
                throw new RuntimeException("Mapping of " + innerName + " not found.");
                */
            }
            return name.equals(other);
        }

        public abstract String getTranslatedName();
    }

    public static void dump() {
        CustomLogger l = MonnefCorePlugin.Log;
        l.printFinest("Dumping " + SrgNames.class.getSimpleName() + ".");

        l.printFinest("Fields:");
        for (Map.Entry<String, String> s : fields.entrySet()) {
            l.printFinest(s.getKey() + " -> " + s.getValue());
        }

        l.printFinest("Methods:");
        for (Map.Entry<String, String> s : methods.entrySet()) {
            l.printFinest(s.getKey() + " -> " + s.getValue());
        }
    }

    public static class SrgMappingClass extends SrgMapping {
        public SrgMappingClass(String name) {
            super(name);
        }

        @Override
        public String getTranslatedName() {
            return innerName;
        }
    }

    public static class SrgMappingField extends SrgMapping {
        public SrgMappingField(String srgName) {
            super(srgName);
        }

        @Override
        public String getTranslatedName() {
            return field(innerName);
        }
    }

    public static class SrgMappingMethod extends SrgMapping {
        public SrgMappingMethod(String srgName) {
            super(srgName);
        }

        @Override
        public String getTranslatedName() {
            return method(innerName);
        }
    }

    public static String getSlashedName(String dottedName) {
        return dottedName.replace('.', '/');
    }

    static {
        if (use()) {
            String mappingsDir;
            String prop = System.getProperty(SYS_PROP_NAME);
            if (prop == null) {
                mappingsDir = "./../conf/";
            } else {
                mappingsDir = prop;
            }

            fields = readMappings(new File(mappingsDir + "fields.csv"));
            methods = readMappings(new File(mappingsDir + "methods.csv"));
            MonnefCorePlugin.Log.printInfo("Read mappings: " + fields.size() + " fields and " + methods.size() + " methods");
            if (fields.size() == 0 || methods.size() == 0) {
                throw new RuntimeException("Some mappings are empty, cannot proceed.");
            }
        } else {
            methods = fields = null;
        }
    }

    public static boolean use() {
        return MonnefCorePlugin.debugEnv;
    }

    public static String field(String srg) {
        if (use()) {
            return fields.get(srg);
        } else {
            return srg;
        }
    }

    public static String method(String srg) {
        if (use()) {
            return methods.get(srg);
        } else {
            return srg;
        }
    }

    private static Map<String, String> readMappings(File file) {
        if (!file.isFile()) {
            throw new RuntimeException("Couldn't find MCP mappings. Please provide system property " + SYS_PROP_NAME);
        }
        try {
            MonnefCorePlugin.Log.printFine("Reading SRG->MCP mappings from " + file);
            return Files.readLines(file, Charsets.UTF_8, new MCPFileParser());
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read SRG->MCP mappings", e);
        }
    }

    private static class MCPFileParser implements LineProcessor<Map<String, String>> {

        private static final Splitter splitter = Splitter.on(',').trimResults();
        private final Map<String, String> map = Maps.newHashMap();
        private boolean foundFirst;

        @Override
        public boolean processLine(String line) throws IOException {
            if (!foundFirst) {
                foundFirst = true;
                return true;
            }

            Iterator<String> splitted = splitter.split(line).iterator();
            try {
                String srg = splitted.next();
                String mcp = splitted.next();
                if (!map.containsKey(srg)) {
                    map.put(srg, mcp);
                }
            } catch (NoSuchElementException e) {
                throw new IOException("Invalid Mappings file!", e);
            }

            return true;
        }

        @Override
        public Map<String, String> getResult() {
            return ImmutableMap.copyOf(map);
        }
    }
}
