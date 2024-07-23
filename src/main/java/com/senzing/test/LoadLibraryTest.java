package com.senzing.test;

import com.sun.jna.Native;

import com.senzing.sdk.*;
import com.senzing.sdk.core.*;

public class LoadLibraryTest {
    public static void main(String[] args) {
        SzEnvironment env = null;
        try {
            LibraryLoader loader = Native.load("kernel32", LibraryLoader.class);
            for (String libName : args) {
                loader.LoadLibraryExA(libName, 0L, 1);
            }

            env = SzCoreEnvironment.newBuilder().build();

            SzProduct product = env.getProduct();

            System.out.println(product.getVersion());
        
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (env != null) env.destroy();
        }

    }
}
