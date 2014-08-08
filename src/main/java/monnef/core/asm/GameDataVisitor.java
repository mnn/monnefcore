package monnef.core.asm;

import monnef.core.asm.lightningHook.InjectLightningEventAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static monnef.core.MonnefCorePlugin.Log;

public class GameDataVisitor extends ClassVisitor {
    private final boolean debugMessages;

    public GameDataVisitor(int api, ClassVisitor cv, boolean debugMessages) {
        super(api, cv);
        this.debugMessages = debugMessages;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

        if (mv != null && "registerItem".equals(name) &&
                "(Lnet/minecraft/item/Item;Ljava/lang/String;Ljava/lang/String;I)I".equals(desc)) {
            Log.printFine("Found registerItem method.");
            mv = new RegisterItemMethodVisitor(api, mv, debugMessages);
        }

        return mv;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
