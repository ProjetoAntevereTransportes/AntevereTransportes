/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

/**
 *
 * @author lucas
 */
public class Debug {

    public static boolean isDebug() {
        return java.lang.management.ManagementFactory.getRuntimeMXBean().
                getInputArguments().toString().indexOf("jdwp") >= 0;
    }
}
