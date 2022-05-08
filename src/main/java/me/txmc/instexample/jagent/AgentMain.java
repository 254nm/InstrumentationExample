package me.txmc.instexample.jagent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import me.txmc.instexample.Main;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author 254n_m
 * @since 5/6/22/ 8:29 PM
 * This file was created as a part of InstrumentationExample
 */
public class AgentMain {

    public static void premain(String agentOps, Instrumentation inst) {

    }

    public static void agentmain(String agentOps, Instrumentation inst) {
        inst.addTransformer(new Transformer(), true);
        try {
            inst.retransformClasses(Main.class);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static class Transformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            if (className == null) return classfileBuffer;
            if (Main.class == classBeingRedefined) {
                ClassPool pool = ClassPool.getDefault();
                pool.appendClassPath(new LoaderClassPath(loader));
                try {
                    System.out.println("Transforming class " + classBeingRedefined.getName());
                    CtClass cc = pool.getCtClass(classBeingRedefined.getName());
                    CtMethod method = cc.getDeclaredMethod("test");
                    method.setBody("{ System.out.println(\"THIS TEST METHOD WAS MODIFIED BY INSTRUMENTATION AT RUNTIME\"); }");
                    cc.detach();
                    return cc.toBytecode();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            return classfileBuffer;
        }
    }
}
