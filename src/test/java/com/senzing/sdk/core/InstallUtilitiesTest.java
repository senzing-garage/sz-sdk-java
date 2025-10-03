package com.senzing.sdk.core;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.senzing.sdk.test.AbstractTest;
import com.senzing.text.TextUtilities;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class InstallUtilitiesTest extends AbstractTest {
    private static final OutputStream NULL_STREAM = new OutputStream() {
        @Override
        public void write(int b) {
            // Do nothing, effectively discarding the byte
        }

        @Override
        public void write(byte[] b) {
            // Do nothing, effectively discarding the byte array
        }

        @Override
        public void write(byte[] b, int off, int len) {
            // Do nothing, effectively discarding the byte array segment
        }
    };

    private static final PrintStream NULL_PRINT_STREAM 
        = new PrintStream(NULL_STREAM);

    private static void nullTest(Runnable task) {
        InstallUtilities.executeWithStreams(NULL_PRINT_STREAM, NULL_PRINT_STREAM, task);
    }

    @BeforeAll
    public void beforeAll() {
        this.beginTests();
        InstallUtilities.disableExit();
    }

    @AfterAll
    public void afterAll() {
        this.endTests();
    }

    @Test
    public void testValidateRuntimeJar() {
        this.performTest(() -> {
            nullTest(() -> InstallUtilities.validateRuntimeSdkJar());
        });
    }

    @Test
    public void testMainHelp() {
        this.performTest(() -> {
            String[] args = { "-h" };
            nullTest(() -> InstallUtilities.main(args));
        });
    }

    @Test
    public void testMainHelpDump() {
        this.performTest(() -> {
            String[] args = { "-h", "-d" };
            nullTest(() -> InstallUtilities.main(args));
        });
    }

    @Test
    public void testMainDoubleHelp() {
        this.performTest(() -> {
            String[] args = { "-h", "-h" };
            nullTest(() -> InstallUtilities.main(args));
        });
    }

    @Test
    public void testMainDoubleExecute() {
        this.performTest(() -> {
            String[] args = { "-x", "-x" };
            nullTest(() -> InstallUtilities.main(args));
        });
    }

    @Test
    public void testMainRepoPath() {
        this.performTest(() -> {
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File repoDir = new File(tempDir, TextUtilities.randomAlphabeticText(10));
            String[] args = { "-r", repoDir.getPath() };
            nullTest(() -> InstallUtilities.main(args));
        });
    }

    @Test
    public void testMainMissingRepoPath() {
        this.performTest(() -> {
            String[] args = { "-r" };
            nullTest(() -> InstallUtilities.main(args));
        });
    }
    
    @Test
    public void testMainDump() {
        this.performTest(() -> {
            String[] args = { "-d" };
            nullTest(() -> InstallUtilities.main(args));
        });
    }

    @Test
    public void testMainDoubleDump() {
        this.performTest(() -> {
            String[] args = { "-d", "-d" };
            nullTest(() -> InstallUtilities.main(args));
        });
    }

    @Test
    public void testMain() {
        this.performTest(() -> {
            String[] args = { };
            nullTest(() -> InstallUtilities.main(args));
        });
    }
}
