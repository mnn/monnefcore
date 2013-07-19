/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.asm.lightningHook;

import monnef.core.asm.CoreTransformer;
import monnef.core.asm.MappedObject;
import monnef.core.asm.ObfuscationHelper;
import monnef.core.external.javassist.CannotCompileException;
import monnef.core.external.javassist.ClassPool;
import monnef.core.external.javassist.CtClass;
import monnef.core.external.javassist.CtMethod;
import monnef.core.external.javassist.expr.ExprEditor;
import monnef.core.external.javassist.expr.MethodCall;

import java.io.ByteArrayInputStream;

import static monnef.core.MonnefCorePlugin.Log;

public class WorldServerTransformer {
    private static String targetClass = ObfuscationHelper.getRealName(MappedObject.C_WORLD_SERVER);
    private static String targetMethodName = ObfuscationHelper.getRealName(MappedObject.M_CAN_LIGHTNING_STRIKE_AT);
    private static final String HOOK_PROCESSOR_CLASS = "monnef.core.event.EventFactory";
    private static final String HOOK_METHOD_NAME = "onLightningGenerated";
    public static final boolean PRINT_DEBUG_STUFF = true;

    public static byte[] transform(byte[] code) {
        logDebug(String.format("target class: %s, target m name: %s, processor class: %s, processor method: %s", targetClass, targetMethodName, HOOK_PROCESSOR_CLASS, HOOK_METHOD_NAME));
        ClassPool pool = ClassPool.getDefault();
        CtClass cc;
        try {
            cc = pool.makeClass(new ByteArrayInputStream(code));
            String methodName = ObfuscationHelper.getRealName(MappedObject.M_TICK_BLOCKS_AND_AMBIANCE);
            CtMethod method = cc.getMethod(methodName, "()V");
            method.instrument(
                    new ExprEditor() {
                        @Override
                        public void edit(MethodCall m)
                                throws CannotCompileException {
                            logDebug(String.format("[Transformer] MethodCall: cname - %s, mname - %s", m.getClassName(), m.getMethodName()));
                            if (m.getClassName().equals(targetClass)
                                    && m.getMethodName().equals(targetMethodName)) {
                                logDebug("Class found, replacing.");
                                m.replace(String.format("{$_ = %s.%s($0, $1, $2, $3, $proceed($$));}", HOOK_PROCESSOR_CLASS, HOOK_METHOD_NAME));
                                Log.printFine("Lightning hook inserted via Javassist.");
                                CoreTransformer.lightningHookApplied = true;
                            }
                        }
                    }
            );

            return cc.toBytecode();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static void logDebug(String msg) {
        if (PRINT_DEBUG_STUFF)
            Log.printFinest("[" + WorldServerTransformer.class.getSimpleName() + "] " + msg);
    }
}
