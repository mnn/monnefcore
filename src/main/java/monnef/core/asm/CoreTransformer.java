/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.asm;

import monnef.core.asm.lightningHook.WorldServerVisitor;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static monnef.core.MonnefCorePlugin.Log;
import static org.objectweb.asm.Opcodes.ASM4;

public class CoreTransformer implements IClassTransformer {
    public static boolean lightningHookApplied = false;

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
        }

        return bytes;
    }
}

