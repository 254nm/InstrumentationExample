package me.txmc.instexample;

import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.lang.management.ManagementFactory;

public class Main {
    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                VirtualMachine vm = VirtualMachine.attach(args[0]);
                vm.loadAgent(getSelf().getAbsolutePath());
                vm.detach();
            } catch (Throwable t) {
                t.printStackTrace();
                System.exit(0);
            }
            System.exit(1);
        }
        new Main().init();
    }

    private void init() {
        test();
        attach();
        test();
    }

    public void test() {
        System.out.println("TEST");
    }
    private void attach() {
        try {
            String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
            String pid = nameOfRunningVM.substring(0, nameOfRunningVM.indexOf('@'));
            String javaPath = System.getProperty("java.home") + "/bin/java"; //The java in $PATH might be a different version than the one we want
            System.out.println(javaPath);
            //Spawn a new java process to attach the java agent to our current JVM because the JVM doesn't allow us to directly attach our own JVM
            Process process = new ProcessBuilder(javaPath, "-jar", getSelf().getAbsolutePath(), pid).start();
            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode == 1) {
                System.out.println("Successfully attached java agent!!");
            } else System.out.println("Failed to attach!");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    private static File getSelf() throws Throwable {
        return new File(Main.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath());
    }
}