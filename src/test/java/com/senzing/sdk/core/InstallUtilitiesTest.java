package com.senzing.sdk.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.senzing.nativeapi.InstallLocations;
import com.senzing.sdk.test.AbstractTest;
import com.senzing.text.TextUtilities;

import static com.senzing.io.IOUtilities.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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

    @Override
    public void performTest(Runnable task) {
        InstallUtilities.executeWithStreams(
            NULL_PRINT_STREAM, NULL_PRINT_STREAM, () -> super.performTest(task));
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
            InstallUtilities.validateRuntimeSdkJar();
        });
    }

    @Test
    public void testMainHelp() {
        this.performTest(() -> {
            String[] args = { "-h" };
            InstallUtilities.main(args);
        });
    }

    @Test
    public void testMainHelpDump() {
        this.performTest(() -> {
            String[] args = { "-h", "-d" };
            InstallUtilities.main(args);
        });
    }

    @Test
    public void testMainDoubleHelp() {
        this.performTest(() -> {
            String[] args = { "-h", "-h" };
            InstallUtilities.main(args);
        });
    }

    @Test
    public void testMainDoubleExecute() {
        this.performTest(() -> {
            String[] args = { "-x", "-x" };
            InstallUtilities.main(args);
        });
    }

    @Test
    public void testMainRepoPath() {
        this.performTest(() -> {
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File repoDir = new File(tempDir, TextUtilities.randomAlphabeticText(10));
            String[] args = { "-r", repoDir.getPath() };
            InstallUtilities.main(args);
        });
    }

    @Test
    public void testMainRepoWithFile() {
        this.performTest(() -> {
            try {
                File tempFile = File.createTempFile("maven-", "-repo.txt");
                String[] args = { "-r", tempFile.getPath() };
                InstallUtilities.main(args);
            } catch (Exception e) {
                fail("Unexpectex excepton", e);
            }
        });
    }

    @Test
    public void testMainMissingRepoPath() {
        this.performTest(() -> {
            String[] args = { "-r" };
            InstallUtilities.main(args);
        });
    }
    
    @Test
    public void testMainDump() {
        this.performTest(() -> {
            String[] args = { "-d" };
            InstallUtilities.main(args);
        });
    }

    @Test
    public void testMainDoubleDump() {
        this.performTest(() -> {
            String[] args = { "-d", "-d" };
            InstallUtilities.main(args);
        });
    }

    @Test
    public void testMainBogus() {
        this.performTest(() -> {
            String[] args = { "-bogus" };
            InstallUtilities.main(args);
        });
    }

    @Test
    public void testMain() {
        this.performTest(() -> {
            String[] args = { };
            InstallUtilities.main(args);
        });
    }

    @Test
    public void testMissingRequiredInfo() {
        this.performTest(() -> {
            InstallUtilities.handleMissingRequiredInfo();
        });
    }

    @Test 
    public void testQuoteNoSpaces() {
        this.performTest(() -> {
            String text = "hello";
            String result = InstallUtilities.quote(text);
            assertEquals(text, result, "Quoted result not as expected");
        });
    }

    @Test 
    public void testQuoteWithSpaces() {
        this.performTest(() -> {
            String text = "hello, world";
            String result = InstallUtilities.quote(text);
            assertEquals("\"" + text + "\"", result, "Quoted result not as expected");
        });
    }

    public List<Arguments> getCommandArrayParameters() {
        try {
            File dummyJavadocJar = File.createTempFile("sz-sdk-", "-javadoc.jar");
            dummyJavadocJar.deleteOnExit();
            File dummySourcesJar = File.createTempFile("sz-sdk-", "-sources.jar");
            dummySourcesJar.deleteOnExit();
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File dummyRepoDir = new File(tempDir, TextUtilities.randomAlphabeticText(10));

            List<Arguments> result = new LinkedList<>();
            result.add(Arguments.of("mvn", null, null, null));
            result.add(Arguments.of("foo", dummyJavadocJar, null, null));
            result.add(Arguments.of("bar", null, dummySourcesJar, null));
            result.add(Arguments.of("phoo", null, null, dummyRepoDir));
            result.add(Arguments.of("all", dummyJavadocJar, dummySourcesJar, dummyRepoDir));
            return result;

        } catch (Exception e) {
            fail("Unexpected exception", e);
            throw new RuntimeException(e);
        }
    }

    public boolean checkBootstrapBuild() {
        Class<InstallUtilities> cls = InstallUtilities.class;
        String prefix = InstallUtilities.JAR_URL_PREFIX;
        String resource = cls.getSimpleName() + ".class";
        return (!cls.getResource(resource).toString().startsWith(prefix) 
                && InstallUtilities.INSTALL_JAR_FILE == null);
    }

    @ParameterizedTest
    @MethodSource("getCommandArrayParameters")
    @DisabledIf(value = "checkBootstrapBuild", 
                disabledReason="This test only runs against completed Senzing builds")
    public void testBuildCommandArray(String mvn, File javadocJar, File sourcesJar, File repoDir) {
        this.performTest(() -> {
            String[] result = InstallUtilities.buildCommandArray(mvn, javadocJar, sourcesJar, repoDir);

            int expectedSize = 8
                + ((javadocJar != null) ? 1 : 0)
                + ((sourcesJar != null) ? 1 : 0)
                + ((repoDir != null) ? 1 : 0);
            
            assertEquals(expectedSize, result.length, "Unexpected array length");
            assertEquals(mvn, result[0], "Unexpected first array element");
            
            int extendedIndex = 8;
            int docsIndex = (javadocJar != null) ? extendedIndex++ : -1;
            int srcIndex = (sourcesJar != null) ? extendedIndex++ : -1;
            int repoIndex = (repoDir != null) ? extendedIndex++ : -1;

            if (docsIndex > 0) {
                String expected = "-Djavadoc=" + InstallUtilities.quote(javadocJar.getPath());
                assertEquals(expected, result[docsIndex], "Unexpected javadocs jar option");
            }
            if (srcIndex > 0) {
                String expected = "-Dsources=" + InstallUtilities.quote(sourcesJar.getPath());
                assertEquals(expected, result[srcIndex], "Unexpected sources jar option");
            }
            if (repoIndex > 0) {
                String expected = "-DlocalRepositoryPath=" + InstallUtilities.quote(repoDir.getPath());
                assertEquals(expected, result[repoIndex], "Unexpected repo dir option");
            }
        });
    }

    @Test 
    public void testDumpInstallInfo() {
        this.performTest(() -> {
            InstallUtilities.dumpInstallInfo();
        });
    }

    @Test 
    public void testWarnFailedJarValidation() {
        this.performTest(() -> {
            InstallUtilities.warnFailedJarValidation();
        });
    }

    @Test 
    public void testWarnNoRuntimeJar() {
        this.performTest(() -> {
            InstallUtilities.warnNoRuntimeJar();
        });
    }

    @Test 
    public void testWarnNoInstallJar() {
        this.performTest(() -> {
            InstallUtilities.warnNoInstallJar();
        });
    }

    public List<Arguments> getRuntimeJarParameters() {
        List<Arguments> result = new LinkedList<>();
        File installDir = InstallLocations.findLocations().getInstallDirectory();
        if (installDir != null && !installDir.getName().equals("dist")) {
            File sdkDir = new File(installDir, "sdk");
            File javaDir = new File(sdkDir, "java");
            File jarFile = new File(javaDir, "sz-sdk.jar");
            if (jarFile.exists() && jarFile.isFile()) {
                result.add(Arguments.of(jarFile, installDir.getParentFile(), jarFile));
            }
        }
        result.add(Arguments.of(null, null, null));

        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        String[] dirNames = { "er", "sdk", "java" };
        for (int depth = 1; depth <= 3; depth++) {
            File rootDir = new File(tempDir, TextUtilities.randomAlphabeticText(10));
            File parentDir = rootDir;
            for (int index = 0; index < depth; index++) {
                parentDir = new File(parentDir, dirNames[index]);
            }
            parentDir.mkdirs();
            try {
                File fakeJar = File.createTempFile("sz-sdk-", ".jar", parentDir);
                result.add(Arguments.of(
                    fakeJar, 
                    (depth == 3) ? rootDir : null, 
                    (depth == 3) ? fakeJar : null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @ParameterizedTest
    @MethodSource("getRuntimeJarParameters") 
    public void testInferSenzingPath(File runtimeJar, File senzingPath, File installJar) {
        this.performTest(() -> {
            File actual = InstallUtilities.inferSenzingPath(runtimeJar);
            assertEquals(senzingPath, actual, "Unexpected Senzing Path from runtime jar: " + runtimeJar);
        });
    }

    @ParameterizedTest
    @MethodSource("getRuntimeJarParameters") 
    public void testInferSenzingInstallJar(File runtimeJar, File senzingPath, File installJar) {
        this.performTest(() -> {
            File actual = InstallUtilities.inferInstallSdkJarFile(runtimeJar);
            assertEquals(installJar, actual, "Unexpected install jar from runtime jar: " + runtimeJar);

        });
    }

    public List<Arguments> getCompareJarParameters() {
        List<Arguments> result = new LinkedList<>();
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File jar1 = new File(tempDir, "sz-sdk-1.jar");
        File jar2 = new File(tempDir, "sz-sdk-2.jar");
        File jar3 = new File(tempDir, "sz-sdk-3.jar");
        File jar4 = new File(tempDir, "sz-sdk-4.jar");
        File jar0 = new File(tempDir, "sz-sdk-0.jar");

        try (FileOutputStream fos1 = new FileOutputStream(jar1);
             FileOutputStream fos2 = new FileOutputStream(jar2);
             OutputStreamWriter osw1 = new OutputStreamWriter(fos1, UTF_8);
             OutputStreamWriter osw2 = new OutputStreamWriter(fos2, UTF_8))
        {
            String text = TextUtilities.randomAlphabeticText(40).toUpperCase();
            osw1.write(text);
            osw2.write(text);
            osw1.flush();
            osw2.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileOutputStream fos1 = new FileOutputStream(jar3);
             FileOutputStream fos2 = new FileOutputStream(jar4);
             OutputStreamWriter osw1 = new OutputStreamWriter(fos1, UTF_8);
             OutputStreamWriter osw2 = new OutputStreamWriter(fos2, UTF_8))
        {
            String text1 = TextUtilities.randomAlphabeticText(30).toUpperCase();
            String text2 = TextUtilities.randomAlphabeticText(30).toLowerCase();
            osw1.write(text1);
            osw2.write(text2);
            osw1.flush();
            osw2.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        result.add(Arguments.of(jar1, jar1, true));
        result.add(Arguments.of(jar1, jar2, true));
        result.add(Arguments.of(jar2, jar1, true));
        result.add(Arguments.of(jar2, jar2, true));
        result.add(Arguments.of(jar3, jar3, true));
        result.add(Arguments.of(jar4, jar4, true));
        result.add(Arguments.of(jar1, jar3, false));
        result.add(Arguments.of(jar1, jar4, false));
        result.add(Arguments.of(jar2, jar3, false));
        result.add(Arguments.of(jar2, jar4, false));
        result.add(Arguments.of(jar3, jar1, false));
        result.add(Arguments.of(jar4, jar1, false));
        result.add(Arguments.of(jar3, jar2, false));
        result.add(Arguments.of(jar4, jar2, false));
        result.add(Arguments.of(jar3, jar4, false));
        result.add(Arguments.of(jar4, jar3, false));
        result.add(Arguments.of(jar0, jar0, null));
        result.add(Arguments.of(jar1, jar0, null));
        result.add(Arguments.of(jar2, jar0, null));
        result.add(Arguments.of(jar3, jar0, null));
        result.add(Arguments.of(jar4, jar0, null));
        result.add(Arguments.of(jar0, jar1, null));
        result.add(Arguments.of(jar0, jar2, null));
        result.add(Arguments.of(jar0, jar3, null));
        result.add(Arguments.of(jar0, jar4, null));
        return result;
    }

    @ParameterizedTest
    @MethodSource("getCompareJarParameters") 
    public void testCompareJarFiles(File installjar, File runtimeJar, Boolean expected) {
        this.performTest(() -> {
            Boolean actual = InstallUtilities.compareJarFiles(installjar, runtimeJar);
            assertEquals(expected, actual, "Unexpected compare result for jar files: "
                + "installJar=[ " + installjar + " ], runtimeJar=[ " + runtimeJar + " ]");
        });
    }

    @Test 
    public void testJarForClass() {
        this.performTest(() -> {
            InstallUtilities.findJarForClass(TextUtilities.class);
        });
    }
    
    @Test 
    public void testExecuteMaven() {
        this.performTest(() -> {
            String[] cmdArray = { "java", "-version" };

            try {
                boolean result = InstallUtilities.executeMaven(cmdArray);

                assertTrue(result, "Unexpected failure for sub-process");

            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }

    @Test 
    public void testExecuteMavenBadOption() {
        this.performTest(() -> {
            String[] cmdArray = { 
                "java", 
                "-fake_option_" + TextUtilities.randomAlphabeticText(5) };

            try {
                boolean result = InstallUtilities.executeMaven(cmdArray);

                assertFalse(result, "Unexpected success for sub-process");

            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }

    @Test 
    public void testExecuteMavenBadExe() {
        this.performTest(() -> {
            String[] cmdArray = { 
                "does_not_exist_" + TextUtilities.randomAlphabeticText(5) };

            try {
                InstallUtilities.executeMaven(cmdArray);

                fail("Unexpected success on sub-process");


            } catch (IOException expected) {
                // expected
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }

}
