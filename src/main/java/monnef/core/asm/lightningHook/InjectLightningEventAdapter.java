/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.asm.lightningHook;

import monnef.core.MonnefCorePlugin;
import monnef.core.asm.CoreTransformer;
import monnef.core.asm.MappedObject;
import monnef.core.asm.ObfuscationHelper;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.ILOAD;

public class InjectLightningEventAdapter extends MethodVisitor {
    public static final String MSG_PREFIX = "[LH] ";
    private ArrayList<Integer> localVars = new ArrayList<Integer>();
    private State state = State.LOOKING;
    private Logger logger = new Logger();

    private enum State {LOOKING, GOT_METHOD, DONE, BROKEN}


    public InjectLightningEventAdapter(MethodVisitor visitor) {
        super(ASM4, visitor);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        if (state == State.DONE) {
            super.visitVarInsn(opcode, var);
            return;
        }

        if (opcode == ILOAD) {
            localVars.add(var);
            logger.log("added local var " + var);
        }
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        if (state == State.DONE) {
            super.visitMethodInsn(opcode, owner, name, desc);
            return;
        }

        if (state == State.LOOKING && ObfuscationHelper.namesAreEqual(name, MappedObject.M_CAN_LIGHTNING_STRIKE_AT)) {
            newState(State.GOT_METHOD);
            logger.log("method found");
        } else {
            localVars.clear();
            logger.log("clearing local vars cache on method call: " + name);
        }
        super.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        if (state == State.DONE) {
            super.visitJumpInsn(opcode, label);
            return;
        }

        if (state == State.GOT_METHOD && opcode == IFEQ) {
            super.visitJumpInsn(opcode, label);
            injectHook(label);
        } else {
            newState(State.LOOKING);
            super.visitJumpInsn(opcode, label);
        }
    }

    private void newState(State newState) {
        if (state != newState) {
            logger.log(String.format("state changed from %s to %s", state, newState));
            state = newState;
        }
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        if (state != State.DONE) {
            printError("not DONE at the end, code has probably changed so the adapter cannot detect right injection place");
            logger.printAll();
        }
    }

    private void injectHook(Label label) {
        if (localVars.size() != 3) {
            newState(State.BROKEN);
            logger.log("expected 3 local vars, got " + localVars.size() + " (" + getVarsForLog() + ")");
            return;
        }
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(ILOAD, localVars.get(0));
        mv.visitVarInsn(ILOAD, localVars.get(1));
        mv.visitVarInsn(ILOAD, localVars.get(2));
        // mv.visitMethodInsn(Opcodes.INVOKESTATIC, "monnef/core/event/EventFactory", "onLightningGenerated", "(Lnet/minecraft/world/World;III)Z");
        String signature = "(L" + ObfuscationHelper.getRealNameSlashed(MappedObject.C_WORLD) + ";III)Z";
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "monnef/core/event/EventFactory", "onLightningGenerated", signature);
        mv.visitJumpInsn(IFEQ, label);
        print("Lightning hook inserted.");
        newState(State.DONE);
        CoreTransformer.lightningHookApplied = true;
    }

    private String getVarsForLog() {
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

    private void print(String msg) {
        MonnefCorePlugin.Log.printInfo(MSG_PREFIX + msg);
    }

    private void printError(String msg) {
        MonnefCorePlugin.Log.printSevere(MSG_PREFIX + msg);
    }

    private class Logger {
        private LinkedList<String> messages = new LinkedList<String>();

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
    }
}
