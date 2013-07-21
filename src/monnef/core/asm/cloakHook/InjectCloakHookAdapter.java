/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.asm.cloakHook;

import monnef.core.asm.CoreTransformer;
import monnef.core.asm.ObfuscationHelper;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static monnef.core.MonnefCorePlugin.Log;
import static monnef.core.asm.MappedObject.C_ENTITY;
import static monnef.core.asm.MappedObject.M_ON_ENTITY_CREATE;
import static monnef.core.asm.ObfuscationHelper.getRealNameSlashed;
import static monnef.core.asm.cloakHook.InjectCloakHookAdapter.State.DONE;
import static monnef.core.asm.cloakHook.InjectCloakHookAdapter.State.LOOKING;
import static monnef.core.asm.cloakHook.InjectCloakHookAdapter.State.READ_UPDATECLOAK;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

public class InjectCloakHookAdapter extends MethodVisitor {
    public State getState() {
        return state;
    }

    enum State {LOOKING, READ_UPDATECLOAK, DONE}

    private State state;

    public InjectCloakHookAdapter(MethodVisitor visitor) {
        super(ASM4, visitor);
        state = LOOKING;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        switch (state) {
            case LOOKING:
                if (opcode == INVOKEVIRTUAL && ObfuscationHelper.namesAreEqual(name, M_ON_ENTITY_CREATE)) {
                    Log.printFine("Found updateCloak method.");
                    state = READ_UPDATECLOAK;
                }
                break;

            case READ_UPDATECLOAK:
                state = LOOKING;
                break;

            case DONE:
                break;
        }

        mv.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        boolean insertHook = false;

        switch (state) {
            case LOOKING:
            case DONE:
                break;

            case READ_UPDATECLOAK:
                //if (opcode == ALOAD && var == 1) {
                if (opcode == ALOAD) {
                    insertHook = true;
                    state = DONE;
                } else {
                    state = LOOKING;
                }
                break;
        }

        if (insertHook) insertHook();
        mv.visitVarInsn(opcode, var);
    }

    private void insertHook() {
        String signature = "(L" + getRealNameSlashed(C_ENTITY) + ";)Z";
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKESTATIC, "monnef/core/CloakHookHandler", "handleUpdateCloak", signature);
        Label dontSkipLabel = new Label();
        mv.visitJumpInsn(Opcodes.IFEQ, dontSkipLabel);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitLabel(dontSkipLabel);
        CoreTransformer.cloakHookApplied = true;
        Log.printFine("Cloak hook inserted.");
    }

}
