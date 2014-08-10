/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.asm;

import monnef.core.MonnefCorePlugin;
import monnef.core.asm.lightningHook.WorldServerVisitor;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static monnef.core.MonnefCorePlugin.Log;
import static org.objectweb.asm.Opcodes.ASM4;

public class CoreTransformer implements IClassTransformer {
    public static boolean lightningHookApplied = false;
    public static boolean gameDataHookApplied = false;

    private static final boolean DEBUG_GAMEDATA_TRANSFORMER = false;

    public CoreTransformer() {
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) return null;

        if (SrgNames.C_WORLD_SERVER.isEqualName(name)) {
            Log.printFine("Found WorldServer class.");
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassReader reader = new ClassReader(bytes);
            ClassVisitor visitor = new WorldServerVisitor(ASM4, writer);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        } else if (name != null) {
            if (name.equals("cpw.mods.fml.common.registry.GameData")) {
                Log.printFine("Found GameData class.");
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                ClassReader reader = new ClassReader(bytes);
                ClassVisitor visitor = new GameDataVisitor(ASM4, writer, DEBUG_GAMEDATA_TRANSFORMER);
                reader.accept(visitor, 0);
                byte[] out = writer.toByteArray();

                if (DEBUG_GAMEDATA_TRANSFORMER) {
                    FileOutputStream os;
                    try {
                        os = new FileOutputStream("mC_GameData.class");
                        os.write(out);
                        os.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return out;
            }
        }

        return bytes;
    }
}

