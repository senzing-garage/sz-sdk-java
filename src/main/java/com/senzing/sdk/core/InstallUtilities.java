package com.senzing.sdk.core;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Utilities for working with the Senzing installation.
 */
final class InstallUtilities {
    /**
     * The thread-local error stream.
     */
    private static final ThreadLocal<PrintStream> ERR = new ThreadLocal<>() {
        protected PrintStream initialValue() {
            return System.err;
        }
    };

    /**
     * The thread-local output stream.
     */
    private static final ThreadLocal<PrintStream> OUT = new ThreadLocal<>() {
        protected PrintStream initialValue() {
            return System.out;
        }
    };

    /**
     * Flag indicating if exit is disabled.
     */
    private static boolean exitDisabled = false;

    /**
     * Disables exiting the application for the purpose of tests.
     */
    static synchronized void disableExit() {
        exitDisabled = true;
    }

    /**
     * Handles exiting when needed accounting for disabled exit setting.
     * 
     * @param exitCode The exit code.
     */
    private static void exit(int exitCode) {
        if (!exitDisabled) {
            System.exit(exitCode);
        }
    }

    /**
     * Private default constructor.
     */
    private InstallUtilities() {
        // do nothing
    }

    /**
     * Executes the specified {@link Runnable} using the specified {@link PrintStream}
     * instances.
     * 
     * @param out The {@link PrintStream} to use in place of {@link System#.out}.
     * @param err The {@link PrintStream} to use in place of {@link System#.err}.
     * @param task The {@link Runnable} task to execute.
     */
    static void executeWithStreams(PrintStream out, PrintStream err, Runnable task) {
        PrintStream initialErr = ERR.get();
        PrintStream initialOut = OUT.get();
        try {
            ERR.set(err);
            OUT.set(out);
            
            task.run();

        } finally {
            ERR.set(initialErr);
            OUT.set(initialOut);
        }
    }

    /**
     * Gets the error {@link PrintStream}.
     * 
     * @return The error {@link PrintStream}.
     */
    static PrintStream getErr() {
        return ERR.get();
    }

    /**
     * Gets the output {@link PrintStream}.
     * 
     * @return The output {@link PrintStream}.
     */
    static PrintStream getOut() {
        return OUT.get();
    }

    /**
     * The UTF-8 character encoding.
     */
    private static final String UTF_8 = "UTF-8";

    /**
     * The Maven POM Properties key for the artifact ID.
     */
    private static final String POM_ARTIFACT_ID_KEY = "artifactId";

    /**
     * The Maven POM Properties key for the group ID.
     */
    private static final String POM_GROUP_ID_KEY = "groupId";
    
    /**
     * The Maven POM Properties key for the version.
     */
    private static final String POM_VERSION_KEY = "version";

    /**
     * The JAR URL resource path to the POM properties file.
     */
    private static final String POM_PROPERTIES_PATH 
        = "/META-INF/maven/com.senzing/sz-sdk/pom.properties";

    /**
     * The prefix for JAR URL's.
     */
    private static final String JAR_URL_PREFIX = "jar:";

    /**
     * The prefix for file URL's.
     */
    private static final String FILE_URL_PREFIX = "file:";

    /**
     * The quoted version key in the szBuildVersion.json file for the version.
     */
    private static final String QUOTED_JSON_VERSION_KEY = "\"VERSION\"";

    /**
     * Text to output when something is unknown.
     */
    private static final String UNKNOWN = "** UNKNOWN **";

    /**
     * The default maven executable.
     */
    private static final String DEFAULT_MAVEN = "mvn";

    /**
     * An enumeration of the operating system types.
     */
    enum OSType {
        /**
         * Represents the Linux operating system type.
         */
        LINUX,

        /**
         * Represents the macOS operating system type.
         */
        MACOS,

        /**
         * Represents the Windows operating system type.
         */
        WINDOWS,

        /**
         * Represents the unknown operating system type.
         */
        UNKNOWN;
    }

    /**
     * The {@link OSType} for the current runtime operating system.
     */
    static final OSType RUNTIME_OS_TYPE;

    static {
        String osName = System.getProperty("os.name");
        OSType osType = null;
        if (osName.toLowerCase().startsWith("windows")) {
            osType = OSType.WINDOWS;

        } else if (osName.toLowerCase().startsWith("linux")) {
            osType = OSType.LINUX;

        } else if (osName.toLowerCase().startsWith("mac")) {
            osType = OSType.MACOS;
            
        } else {
            getErr().println("WARNING: Unrecognized operating system: " + osName);
            osType = OSType.UNKNOWN;
        }
        RUNTIME_OS_TYPE = osType;
    }

    /**
     * The name of the library file for the operating system.
     */
    private static final String LIBRARY_FILE_NAME;

    static {
        String libName = null;
        switch (RUNTIME_OS_TYPE) {
            case WINDOWS:
                libName = "Sz.dll";
                break;
            case LINUX:
                libName = "libSz.so";
                break;
            case MACOS:
                libName = "libSz.dylib";
                break;
            default:
                libName = null;
        }
        LIBRARY_FILE_NAME = libName;
    }

    /**
     * The environment variable for the library path.
     */
    private static final String LIBRARY_PATH_ENV_VARIABLE;

    static {
        String  pathVar  = null;

        switch (RUNTIME_OS_TYPE) {
            case WINDOWS:
                pathVar = "Path";
                break;
            case LINUX:
                pathVar = "LD_LIBRARY_PATH";
                break;
            case MACOS:
                pathVar = "DYLD_LIBRARY_PATH";
                break;
            default:
                pathVar = null;
        }

        LIBRARY_PATH_ENV_VARIABLE = pathVar;
    }
    
    /**
     * The environment variable for the executable path.
     */
    private static final String EXECUTABLE_PATH_ENV_VARIABLE;

    static {
        // determine the name of the environment variable
        String  pathVar = null;

        switch (RUNTIME_OS_TYPE) {
            case WINDOWS:
                pathVar = "Path";
                break;
            case LINUX:
            case MACOS:
                pathVar = "PATH";
                break;
            default:
                pathVar = null;
        }

        EXECUTABLE_PATH_ENV_VARIABLE = pathVar;
    }
    /**
     * Gets the library path for the current operating system platform
     * as a {@link List} of {@link File} objects.
     * 
     * @return The library path as a {@link List} of {@link File} objects.
     */
    static List<File> getLibraryPath() {
        // determine the name of the environment variable
        if (LIBRARY_PATH_ENV_VARIABLE == null) {
            return new LinkedList<>();
        }

        return parsePathVariable(LIBRARY_PATH_ENV_VARIABLE);
    }

    /**
     * Utility function to parse a path-like variable and return a
     * {@link List} of {@link File} objects describing the directories
     * in that path.
     * 
     * @param envVariable The environment variable.
     * 
     * @return The path as a {@link List} of {@link File} objects.
     */
    private static List<File> parsePathVariable(String envVariable) {
        List<File> result = new LinkedList<>();

        // get the environment variable
        String envVal = System.getenv(envVariable);
        if (envVal == null || envVal.trim().length() == 0) {
            // return an empty list
            return result;
        }

        // get the path separator
        String pathSep = System.getProperty("path.separator");

        // parse the library path
        //
        // NOTE: We do NOT allow directories containing the path separator and
        // any escaping of those characters.
        // 
        // On Windows, you can technically include directories that contain
        // semicolons in the Path and while Windows can find executables this way,
        // the DLL search routine cannot handle the double-quotes needed for
        // escaping semicolons in the Path.
        //
        // On Linux and macOS, the POSIX function for finding the the shared
        // libraries does NOT support escaping of the colon character.
        //
        String literalRegexPattern = Pattern.quote(pathSep);
        String[] tokens = envVal.split(literalRegexPattern);
        
        // iterate through the tokens
        for (String token : tokens) {
            result.add(new File(token));
        }

        // return the result
        return result;

    }

    /**
     * The <b>unmodifiable</b> {@link List} representing the library path.
     */
    static final List<File> LIBRARY_PATH = Collections.unmodifiableList(getLibraryPath());

    /**
     * Gets the executable path for the current operating system platform
     * as a {@link List} of {@link File} objects.
     * 
     * @return The executable path as a {@link List} of {@link File} objects.
     */
    static List<File> getExecutablePath() {
        if (EXECUTABLE_PATH_ENV_VARIABLE == null) {
            return new LinkedList<>();
        }
        return parsePathVariable(EXECUTABLE_PATH_ENV_VARIABLE);
    }

    /**
     * The <b>unmodifiable</b> {@link List} representing the executable path.
     */
    static final List<File> EXECUTABLE_PATH = Collections.unmodifiableList(getExecutablePath());

    /**
     * Finds the Senzing native shared library {@link File}.
     * 
     * @return The {@link File} representing the Senzing native shared library,
     *         or <code>null</code> if it could not be found in the library path.
     */
    static File findSzLibraryFile() {
        if (LIBRARY_FILE_NAME == null) {
            return null;
        }

        // loop through the library path until found
        for (File directory : LIBRARY_PATH) {
            // skip non-existent directories
            if (!directory.exists()) {
                continue;
            }

            // skip files that are not directories
            if (!directory.isDirectory()) {
                continue;
            }

            // check the directory
            File libFile = new File(directory, LIBRARY_FILE_NAME);
            if (libFile.exists()) {
                try {
                    return libFile.getCanonicalFile();
                } catch (Exception e) {
                    return null;
                }
            }
        }

        // return null if not found
        return null;
    }

    /**
     * The {@link File} representing the native Senzing shared library.
     */
    static final File SENZING_LIBRARY_FILE = findSzLibraryFile();

    /**
     * Finds the Senzing installation root path based on the location of
     * the {@link #SENZING_LIBRARY_FILE} and if that fails, it tries to 
     * infer it from the current runtime jar file.
     * 
     * @return The {@link File} representing the directory that is the
     *         root of the Senzing installation, or <code>null</code> if
     *         not found.
     */
    static File findSenzingPath() {
        File dir = findSenzingPathFromLib();
        if (dir != null) {
            return dir;
        }

        return inferSenzingPath(findRuntimeSdkJarFile());
    }

    /**
     * Attempts to infer the senzing path from the runtime JAR file.
     * 
     * @param runtimeJar The JAR file from which to infer the path.
     * 
     * @return The inferred Senzing path, or <code>null</code> if
     *         unsuccessful.
     */
    static File inferSenzingPath(File runtimeJar) {
        // try to use the runtime JAR file instead
        if (runtimeJar == null) {
            return null;
        }

        File javaDir = runtimeJar.getParentFile();
        if (javaDir == null || !"java".equalsIgnoreCase(javaDir.getName())) {
            return null;
        }

        File sdkDir = javaDir.getParentFile();
        if (sdkDir == null || !"sdk".equalsIgnoreCase(sdkDir.getName())) {
            return null;
        }

        File erDir = sdkDir.getParentFile();
        if (erDir == null || !"er".equalsIgnoreCase(erDir.getName())) {
            return null;
        }

        // get the parent directory (which may be null, and if so then not found)
        return erDir.getParentFile();
    }

    /**
     * Finds the Senzing installation root path based on the location of
     * the {@link #SENZING_LIBRARY_FILE}.
     * 
     * @return The {@link File} representing the directory that is the
     *         root of the Senzing installation, or <code>null</code> if
     *         not found.
     */
    static File findSenzingPathFromLib() {
        if (SENZING_LIBRARY_FILE == null) {
            return null;
        }
        File libDir = SENZING_LIBRARY_FILE.getParentFile();
        if (libDir == null || !libDir.exists() || !libDir.getName().equalsIgnoreCase("lib")) {
            // unexpected location for library -- it does not appear to
            // be located within the Senzing installation
            return null;
        }
        File erDir = libDir.getParentFile();
        if (erDir == null || !erDir.exists() || !erDir.getName().equalsIgnoreCase("er")) {
            // unexpected location for "lib" directory -- it does not appear to
            // be located within the Senzing installation
            return null;
        }
        File senzingDir = erDir.getParentFile();
        if (senzingDir == null || !senzingDir.exists()) {
            // unexpected location for the "er" directory -- it does not appear
            // to be located within the Senzing installation
            return null;
        }
        try {
            return senzingDir.getCanonicalFile();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * The {@link File} representing the root Senzing installation path.
     */
    static final File SENZING_PATH = findSenzingPath();

    /**
     * Finds the Senzing build version JSON file.
     * 
     * @return THe Senzing build version JSON file.
     */
    static File findBuildVersionFile() {
        if (SENZING_PATH == null) {
            return null;
        }
        File erDir = new File(SENZING_PATH, "er");
        if (!erDir.exists() || !erDir.isDirectory()) {
            return null;
        }
        File versionFile = new File(erDir, "szBuildVersion.json");
        if (!versionFile.exists() || !versionFile.isFile()) {
            return null;
        }
        try {
            return versionFile.getCanonicalFile();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * The {@link File} representing the Senzing build version JSON file.
     */
    static final File BUILD_VERSION_FILE = findBuildVersionFile();

    /**
     * Extracts the version number from the szBuildVersion.json file.
     * @return The build version inferred from the installation.
     */
    static String getInstallBuildVersion() {
        PrintStream err = getErr();
        if (BUILD_VERSION_FILE == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(BUILD_VERSION_FILE);
             InputStreamReader isr = new InputStreamReader(fis, UTF_8);
             BufferedReader br = new BufferedReader(isr)) 
        {
            for (int c = br.read(); c > 0; c = br.read()) {
                sb.append((char) c);
            }

        } catch (Exception e) {
            err.println("Failed to read build version file: " + BUILD_VERSION_FILE);
            err.println(e.getMessage());
            //e.printStackTrace(err);
            return null;
        }

        String fileContent = sb.toString();

        String jsonKey = QUOTED_JSON_VERSION_KEY;
        int index = fileContent.indexOf(jsonKey);
        if (index < 0 || ((index + jsonKey.length()) >= fileContent.length())) 
        {
            return null;
        }

        index += jsonKey.length();

        // eat whitespace
        index = eatWhiteSpace(fileContent, index);
        
        // check for a colon
        if (fileContent.charAt(index) != ':') {
            return null;
        }
        index++;

        // eat whitespace again
        index = eatWhiteSpace(fileContent, index);
        
        // check for a double-quote
        if (fileContent.charAt(index) != '"') {
            return null;
        }

        // capture the content within the double quotes
        int startIndex = index + 1;
        int endIndex = fileContent.indexOf('"', startIndex);
        if (endIndex < 0) {
            return null;
        }

        // return the version
        return fileContent.substring(startIndex, endIndex).trim();
    }

    /**
     * Utility method to eat white space within text.
     * 
     * @param text The text to traverse.
     * @param index The starting index from which to begin traversal.
     * 
     * @return The index at which the whitespace ends.
     */
    static int eatWhiteSpace(String text, int index) {
        while (index < text.length() && Character.isWhitespace(text.charAt(index)))
        {
            index++;
        }
        return index;
    }

    /**
     * The {@link String} build version of the Senzing installation as 
     * extracted from the szBuildVersion.json file.
     */
    static final String INSTALL_BUILD_VERSION = getInstallBuildVersion();

    /**
     * Gets the {@link File} describing the sz-sdk.jar file from the
     * Senzing installation and if not found, tries to infer it from the
     * current runtime jar file.
     * 
     * @return The {@link File} describing the sz-sdk.jar file from the
     *         Senzing installation, or <code>null</code> if it could not
     *         be found.
     */
    static File findInstallSdkJarFile() {
        File file = findInstallSdkJarFileFromPath();
        if (file != null) {
            return file;
        }

        return inferInstallSdkJarFile(findRuntimeSdkJarFile());
    }

    /**
     * Attempts to infer the Senzing install SDK jar file
     * from the runtime SDK jar file.
     * 
     * @param runtimeJar The JAR file from which to infer the install jar.
     * 
     * @return The inferred install SDK jar file, or <code>null</code>
     *         if unsuccessful.
     */
    static File inferInstallSdkJarFile(File runtimeJar) {
        // try to use the runtime JAR file instead
        if (runtimeJar == null) {
            return null;
        }

        File javaDir = runtimeJar.getParentFile();
        if (javaDir == null || !"java".equalsIgnoreCase(javaDir.getName())) {
            return null;
        }

        File sdkDir = javaDir.getParentFile();
        if (sdkDir == null || !"sdk".equalsIgnoreCase(sdkDir.getName())) {
            return null;
        }

        File erDir = sdkDir.getParentFile();
        if (erDir == null || !"er".equalsIgnoreCase(erDir.getName())) {
            return null;
        }

        return runtimeJar;
    }

    /**
     * Gets the {@link File} describing the sz-sdk.jar file from the
     * Senzing installation.
     * 
     * @return The {@link File} describing the sz-sdk.jar file from the
     *         Senzing installation, or <code>null</code> if it could not
     *         be found.
     */
    static File findInstallSdkJarFileFromPath() {
        if (SENZING_PATH == null) {
            return null;
        }
        File erDir = new File(SENZING_PATH, "er");
        if (!erDir.exists() || !erDir.isDirectory()) {
            return null;
        }
        File sdkDir = new File(erDir, "sdk");
        if (!sdkDir.exists() || !sdkDir.isDirectory()) {
            return null;
        }
        File javaDir = new File(sdkDir, "java");
        if (!javaDir.exists() || !javaDir.isDirectory()) {
            return null;
        }
        File jarFile = new File(javaDir, "sz-sdk.jar");
        if (!jarFile.exists() || !jarFile.isFile()) {
            return null;
        }
        try {
            return jarFile.getCanonicalFile();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * The {@link File} representing the Senzing build version JSON file.
     */
    static final File INSTALL_JAR_FILE = findInstallSdkJarFile();

    /**
     * Gets the {@link Map} of {@link String} keys to {@link String} values
     * representing the Maven properties from the installed JAR file.
     * 
     * @return The {@link Map} of {@link String} keys to {@link String} values
     *         representing the Maven properties from the installed JAR file,
     *         or an empty {@link Map} if it cannot be found.
     */
    static Map<String, String> getInstallJarMavenProperties() {
        Map<String, String> result = new LinkedHashMap<>();
        if (INSTALL_JAR_FILE == null) {
            return result;
        }
        try {
            URL jarUrl = new URL(
                JAR_URL_PREFIX 
                + INSTALL_JAR_FILE.toURI().toURL().toString()
                + "!" + POM_PROPERTIES_PATH);
                
            try (InputStream is = jarUrl.openStream()) {
                Properties props = new Properties();
                props.load(is);

                props.forEach((key, value) -> {
                    result.put(key.toString(), value.toString());
                });

                return result;
            }
            
        } catch (Exception e) {
            return result;
        }

    }

    /**
     * The <b>unmodifiable</b> {@link Map} representing Maven POM 
     * properties from the SDK jar in the Senzing installation.
     */
    static final Map<String, String> INSTALL_JAR_MAVEN_PROPERTIES 
        = Collections.unmodifiableMap(getInstallJarMavenProperties());

    /**
     * The Maven artifact ID for the SDK jar file in the Senzing installation.
     */
    static final String INSTALL_JAR_MAVEN_ARTIFACT_ID
        = INSTALL_JAR_MAVEN_PROPERTIES.get(POM_ARTIFACT_ID_KEY);

    /**
     * The Maven group ID for the SDK jar file in the Senzing installation.
     */
    static final String INSTALL_JAR_MAVEN_GROUP_ID
        = INSTALL_JAR_MAVEN_PROPERTIES.get(POM_GROUP_ID_KEY);

    /**
     * The Maven version for the SDK jar file in the Senzing installation.
     */
    static final String INSTALL_JAR_MAVEN_VERSION
        = INSTALL_JAR_MAVEN_PROPERTIES.get(POM_VERSION_KEY);
    
    /**
     * Finds the path to the current runtime's SDK jar file.
     * 
     * @return The {@link File} representing the current runtime's SDK jar file.
     */
    static File findRuntimeSdkJarFile() {
        return findJarForClass(InstallUtilities.class);
    }

    /**
     * Finds the JAR file for the specified class object.
     * @param cls The class for which the JAR is requested.
     * @return The {@link File} representing the JAR for the class,
     *         or <code>null</code> if not found.
     */
    static File findJarForClass(Class<?> cls) {
        String resource = cls.getSimpleName() + ".class";
        String url = cls.getResource(resource).toString();

        // ensure it starts with the jar URL prefix
        if (!url.startsWith(JAR_URL_PREFIX)) {
            return null;
        }

        // strip the jar URL prefix
        url = url.substring(JAR_URL_PREFIX.length());

        // ensure we have a file URL remaining
        if (!url.startsWith(FILE_URL_PREFIX)) {
            return null;
        }

        // find the end of the file URL
        int index = url.indexOf('!');

        if (index < 0) {
            return null;
        }

        // get the file URL
        url = url.substring(0, index);

        try {
            URL fileUrl = new URL(url);
            return Paths.get(fileUrl.toURI()).toFile().getCanonicalFile();

        } catch (Exception e) {
            return null;
        }

    }

    /**
     * The {@link File} representing the current runtime's SDK jar file.
     */
    static final File RUNTIME_JAR_FILE = findRuntimeSdkJarFile();

    /**
     * Gets the {@link Map} of {@link String} keys to {@link String} values
     * representing the Maven properties from the current runtime SDK JAR file.
     * 
     * @return The {@link Map} of {@link String} keys to {@link String} values
     *         representing the Maven properties from the current runtime SDK
     *         JAR file, or an empty {@link Map} if it cannot be found.
     */
    static Map<String, String> getRuntimeJarMavenProperties() {
        Map<String, String> result = new LinkedHashMap<>();
        try {
            Class<InstallUtilities> cls = InstallUtilities.class;
            try (InputStream is = cls.getResourceAsStream(POM_PROPERTIES_PATH))
            {
                Properties props = new Properties();
                props.load(is);

                props.forEach((key, value) -> {
                    result.put(key.toString(), value.toString());
                });

                return result;
            }
            
        } catch (Exception e) {
            return result;
        }

    }

    /**
     * The <b>unmodifiable</b> {@link Map} representing Maven POM 
     * properties from the current runtime's SDK jar.
     */
    static final Map<String, String> RUNTIME_JAR_MAVEN_PROPERTIES 
        = Collections.unmodifiableMap(getRuntimeJarMavenProperties());

    /**
     * The Maven artifact ID for the current runtime's SDK jar file.
     */
    static final String RUNTIME_JAR_MAVEN_ARTIFACT_ID
        = RUNTIME_JAR_MAVEN_PROPERTIES.get(POM_ARTIFACT_ID_KEY);

    /**
     * The Maven group ID for the current runtime's SDK jar file.
     */
    static final String RUNTIME_JAR_MAVEN_GROUP_ID
        = RUNTIME_JAR_MAVEN_PROPERTIES.get(POM_GROUP_ID_KEY);

    /**
     * The Maven version for the current runtime's SDK jar file.
     */
    static final String RUNTIME_JAR_MAVEN_VERSION
        = RUNTIME_JAR_MAVEN_PROPERTIES.get(POM_VERSION_KEY);

    /**
     * Finds the Maven executable if available, otherwise returns 
     * {@link #DEFAULT_MAVEN}.
     * 
     * @return The path to the Maven executable or {@link #DEFAULT_MAVEN}
     *         if Maven cannot be found.
     */
    static File findMaven() {
        if (EXECUTABLE_PATH == null || EXECUTABLE_PATH.isEmpty()) {
            return null;
        }
        try {
            for (File directory : EXECUTABLE_PATH) {
                File mvnFile = new File(directory, DEFAULT_MAVEN);
                if (mvnFile.exists()) {
                    if (Files.isExecutable(mvnFile.toPath())) {
                        return mvnFile.getCanonicalFile();
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Gets the {@link File} representing the sz-sdk-javadoc.jar from the
     * Senzing installation.
     * 
     * @return The {@link File} representing the sz-sdk-javadoc.jar from the
     *         Senzing installation, or <code>null</code> if it cannot be found.
     */
    static File findJavadocJarFile() {
        if (INSTALL_JAR_FILE == null) {
            return null;
        }
        File parentDir = INSTALL_JAR_FILE.getParentFile();
        File javadocJarFile = new File(parentDir, "sz-sdk-javadoc.jar");
        if (!javadocJarFile.exists() || !javadocJarFile.isFile()) {
            return null;
        }
        return javadocJarFile;
    }

    /**
     * Gets the {@link File} representing the sz-sdk-sources.jar from the
     * Senzing installation.
     * 
     * @return The {@link File} representing the sz-sdk-sources.jar from the
     *         Senzing installation, or <code>null</code> if it cannot be found.
     */
    static File findSourcesJarFile() {
        if (INSTALL_JAR_FILE == null) {
            return null;
        }
        File parentDir = INSTALL_JAR_FILE.getParentFile();
        File sourcesJarFile = new File(parentDir, "sz-sdk-sources.jar");
        if (!sourcesJarFile.exists() || !sourcesJarFile.isFile()) {
            return null;
        }
        return sourcesJarFile;
    }

    /**
     * Print warning message when trying to validation the 
     * runtime jar file and there is no install jar.
     */    
    static void warnNoInstallJar() {
        PrintStream err = getErr();
        err.println();
        err.println(
            "WARNING: Unable to find the sz-sdk.jar file from the Senzing installation.");
        err.println(
            "Unable to validate the runtime sz-sdk.jar versus the installation.");

        err.println();
        err.println("Senzing install path    : " 
            + (SENZING_PATH == null ? UNKNOWN : SENZING_PATH));
        err.println();
        err.println("Senzing install version : " 
            + (INSTALL_BUILD_VERSION == null ? UNKNOWN : INSTALL_BUILD_VERSION));
        err.println();
        err.println("Senzing native library  : " 
            + (SENZING_LIBRARY_FILE == null ? UNKNOWN : SENZING_LIBRARY_FILE));
        err.println();
    }

    /**
     * Print error message when trying to validation the 
     * runtime jar file and there is none.
     */
    static void warnNoRuntimeJar() {
        PrintStream err = getErr();
        err.println();
        err.println(
            "WARNING: Unable to find the current runtime's sz-sdk.jar file.");
        err.println(
            "Unable to validate the runtime sz-sdk.jar versus the installation");

        err.println();
        err.println("Senzing install path    : " 
            + (SENZING_PATH == null ? UNKNOWN : SENZING_PATH));
        err.println();
        err.println("Senzing install version : " 
            + (INSTALL_BUILD_VERSION == null ? UNKNOWN : INSTALL_BUILD_VERSION));
        err.println();
        String installVersion = (INSTALL_JAR_MAVEN_VERSION == null) 
                                ? UNKNOWN : INSTALL_JAR_MAVEN_VERSION;

        err.println("Installed sz-sdk.jar    : " + INSTALL_JAR_FILE
            + " (version " + installVersion + ")");
        err.println();
        err.println("Senzing native library  : " 
            + (SENZING_LIBRARY_FILE == null ? UNKNOWN : SENZING_LIBRARY_FILE));
        err.println();
    }

    /**
     * Validates the current runtime sz-sdk.jar versus the one from the 
     * Senzing installation.
     */
    static void validateRuntimeSdkJar() {
        PrintStream err = getErr();
        if (INSTALL_JAR_FILE == null) {
            warnNoInstallJar();
            return;
        }

        if (RUNTIME_JAR_FILE == null) {
            warnNoRuntimeJar();
            return;
        }

        compareJarFiles(INSTALL_JAR_FILE, RUNTIME_JAR_FILE);
    }

    /**
     * Compares the install jar file against the runtime jar file
     * to see if they are identical.
     * 
     * @param installJar The {@link File} for the install jar.
     * @param runtimeJar The {@link File} for the runtime jar.
     * @return <code>true</code> if the same, otherwise 
     *         <code>false</code>,
     */
    static Boolean compareJarFiles(File installJar, File runtimeJar) {
        PrintStream err = getErr();

        // if both files exist perform basic checks
        if (installJar.exists() && runtimeJar.exists()) {
            // if we have the same file then we are done here
            if (installJar.equals(runtimeJar)) {
                return Boolean.TRUE;
            }

            // compare the file sizes
            if (installJar.length() != runtimeJar.length()) 
            {
                warnFailedJarValidation();
                return Boolean.FALSE;
            }
        }

        // compare the JAR contents
        try (FileInputStream fis1 = new FileInputStream(installJar);
             FileInputStream fis2 = new FileInputStream(runtimeJar);
             BufferedInputStream bis1 = new BufferedInputStream(fis1);
             BufferedInputStream bis2 = new BufferedInputStream(fis2))
        {
            while (true) {
                int c1 = bis1.read();
                int c2 = bis2.read();

                if (c1 != c2) {
                    warnFailedJarValidation();
                    return Boolean.FALSE;
                }

                // break if either EOF -- if they EOF at the same time then
                // the files are the same
                if (c1 < 0 || c2 < 0) {
                    break;
                }
            }

            return Boolean.TRUE;

        } catch (Exception e) {
            err.println();
            err.println(
                "WARNING: Unable to validate the current runtime sz-sdk.jar versus the "
                + "sz-sdk.jar from the Senzing installation due to an exception:");
            err.println();
            err.println(e.getMessage());
            err.println();
            dumpInstallInfo();
            return null;
        }
    }

    /**
     * Outputs the warning if the JAR files do not match.
     * 
     */
    static void warnFailedJarValidation() {
        PrintStream err = getErr();
        err.println();
        err.println("-----------------------------------------------------------");
        err.println(
            "*** WARNING ***: The current runtime sz-sdk.jar does NOT match the supported "
            + "sz-sdk.jar from the Senzing installation.");
        err.println();

        dumpInstallInfo();
        err.println("-----------------------------------------------------------");
    }

    /**
     * Outputs the general installation info.
     */
    static void dumpInstallInfo() {
        PrintStream out = getOut();
        String installVersion = (INSTALL_JAR_MAVEN_VERSION == null) 
                                ? UNKNOWN : INSTALL_JAR_MAVEN_VERSION;
        String runtimeVersion = (RUNTIME_JAR_MAVEN_VERSION == null) 
                                ? UNKNOWN : RUNTIME_JAR_MAVEN_VERSION;

        out.println("Installed sz-sdk.jar (supported) : " + INSTALL_JAR_FILE
            + " (version " + installVersion + ")");

        out.println("Runtime sz-sdk.jar (current)     : " + RUNTIME_JAR_FILE
            + " (version " + runtimeVersion + ")");


        out.println();
        out.println("Senzing install path    : " 
            + (SENZING_PATH == null ? "UNKNOWN" : SENZING_PATH));
        out.println();
        out.println("Senzing install version : " 
            + (INSTALL_BUILD_VERSION == null ? "UNKNOWN" : INSTALL_BUILD_VERSION));
        out.println();
        out.println("Senzing native library  : " 
            + (SENZING_LIBRARY_FILE == null ? "UNKNOWN" : SENZING_LIBRARY_FILE));
        out.println();
    }

    /**
     * Dumps out all installation info.
     */
    private static void dumpAllInstallInfo() {
        PrintStream out = getOut();
        out.println();
        out.println("Senzing Path: " + SENZING_PATH);
        out.println();
        out.println("Senzing Library File: " + SENZING_LIBRARY_FILE);

        out.println();
        out.println("Senzing Build Version File: " + BUILD_VERSION_FILE);

        out.println();
        out.println("Senzing Install Build Version: " + INSTALL_BUILD_VERSION);
        
        out.println();
        out.println("Senzing Install SDK JAR File: " + INSTALL_JAR_FILE);

        out.println();
        out.println("Senzing Install SDK JAR Artifact ID: " 
                            + INSTALL_JAR_MAVEN_ARTIFACT_ID);

        out.println();
        out.println("Senzing Install SDK JAR Group ID: " 
                            + INSTALL_JAR_MAVEN_GROUP_ID);

        out.println();
        out.println("Senzing Install SDK JAR Version: " 
                            + INSTALL_JAR_MAVEN_VERSION);

        out.println();
        out.println("Runtime Senzing SDK JAR File: " 
                            + RUNTIME_JAR_FILE);

        out.println();
        out.println("Runtime Senzing SDK JAR Artifact ID: " 
                            + RUNTIME_JAR_MAVEN_ARTIFACT_ID);

        out.println();
        out.println("Runtime Senzing SDK JAR Group ID: " 
                            + RUNTIME_JAR_MAVEN_GROUP_ID);

        out.println();
        out.println("Runtime Senzing SDK JAR Version: " 
                            + RUNTIME_JAR_MAVEN_VERSION);

        out.println();
    }

    /**
     * Quotes the text if it contains spaces.
     * 
     * @param text The text to quote.
     * @return The optionally quoted text.
     */
    static String quote(String text) {
        if (!text.contains(" ")) {
            return text;
        }
        return ("\"" + text + "\"");
    }

    /**
     * Called from main() if required information is missing.
     */
    static void handleMissingRequiredInfo() {
        PrintStream err = getErr();
        err.println();
        err.println("ERROR: Unable to determine required installation information:");
        if (INSTALL_JAR_FILE == null) {
            err.println("  - Senzing Install SDK Jar File");
        }
        if (INSTALL_JAR_MAVEN_ARTIFACT_ID == null) {
            err.println("  - Senzing Install SDK Jar Artifact ID");
        }
        if (INSTALL_JAR_MAVEN_GROUP_ID == null) {
            err.println("  - Senzing Install SDK Jar Group ID");
        }
        if (INSTALL_JAR_MAVEN_VERSION == null) {
            err.println("  - Senzing Install SDK Jar Version");
        }
        err.println();
        exit(1);
    }

    /**
     * Builds the execution command array with the specified parameters.
     * 
     * @param mvn The maven command as a {@link String}.
     * @param javadocJar The {@link File} for the javadoc JAR or <code>null</code>.
     * @param sourcesJar The {@link File} for the sources JAR or <code>null</code>.
     * @param repositoryPath The {@link File} for the repository directory or <code>null</code>.
     * @return The array of {@link String} for the command-array.
     */
    static String[] buildCommandArray(String    mvn, 
                                      File      javadocJar, 
                                      File      sourcesJar,
                                      File      repositoryPath) 
    {
        List<String> cmdList = new LinkedList<>();
        cmdList.add(mvn);
        cmdList.add("install:install-file");
        cmdList.add("-Dfile=" + quote(INSTALL_JAR_FILE.getPath()));
        cmdList.add("-DgroupId=" + INSTALL_JAR_MAVEN_GROUP_ID);
        cmdList.add("-DartifactId=" + INSTALL_JAR_MAVEN_ARTIFACT_ID);
        cmdList.add("-Dversion=" + INSTALL_JAR_MAVEN_VERSION);
        cmdList.add("-Dpackaging=jar");
        if (javadocJar != null) {
            cmdList.add("-Djavadoc=" + quote(javadocJar.getPath()));
        }
        if (sourcesJar != null) {
            cmdList.add("-Dsources=" + quote(sourcesJar.getPath()));
        }
        if (repositoryPath != null) {
            cmdList.add("-DlocalRepositoryPath=" + quote(repositoryPath.getPath()));
        }

        return cmdList.toArray(new String[cmdList.size()]);
    }

    /**
     * Executes the maven command array.
     * 
     * @param cmdArray The array of {@link String} arguments for
     *                 sub-process exceution.
     * @return <code>true</code> if successful, otherwise <code>false</code>.
     */
    static boolean executeMaven(String[] cmdArray) 
        throws IOException, InterruptedException
    {
        PrintStream err = getErr();
        PrintStream out = getOut();

        Runtime runtime = Runtime.getRuntime();
        out.println();
        out.println("Executing...");
        out.println();
        Process process = runtime.exec(cmdArray);
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            err.println();
            err.println("Failed to execute maven installation.");
            err.println();
            exit(1);
            return false;

        } else {
            out.println();
            out.println("Success.");
            out.println();
            return true;
        }
    }

    /**
     * Main function for handling maven install:install-file operations.
     * 
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        PrintStream err = getErr();
        PrintStream out = getOut();
        try {
            if (RUNTIME_OS_TYPE == OSType.UNKNOWN) {
                err.println();
                err.println("ERROR: Unrecognized/unsupported operating system: " 
                    + System.getProperty("os.name"));
                exit(1);
                return;
            }
            
            Set<String> helpArgs = Set.of("-h", "--help");
            if (args.length == 1 && helpArgs.contains(args[0])) {
                out.println();
                out.println("USAGE: java -jar sz-sdk.jar [options]");
                out.println();
                out.println(
                    "Options:");
                out.println(
                    " [none]    : Output the Maven command-line for installing sz-sdk.jar in your");
                out.println(
                    "             local file-system Maven repository (e.g.: [home]/.m2/repository).");
                out.println();
                out.println(
                    " -h        : Display this help message (must be the only option specified).");
                out.println();
                out.println(
                    " -d        : Display Senzing install info (must be the only option specified).");
                out.println();
                out.println(
                    " -x        : Attempt to execute the generated Maven install command.");
                out.println(
                    "             NOTE: This requires Maven is installed and found in the path");
                out.println();
                out.println(
                    " -r <path> : Optionally specify the path to the local Maven repository.");
                out.println(
                    "             NOTE: Omit to use the default (e.g.: [home]/.m2/repository)");
                out.println();
                return;

            } else if (args.length == 1 && "-d".equals(args[0])) {
                dumpAllInstallInfo();
                return;
            }

            File mvnFile = findMaven();
            if (mvnFile == null) {
                out.println();
                out.println("WARNING: " + DEFAULT_MAVEN + " executable was NOT found in path");
            }

            // check if the required install information is missing
            if (INSTALL_JAR_FILE == null || INSTALL_JAR_MAVEN_ARTIFACT_ID == null
                || INSTALL_JAR_MAVEN_GROUP_ID == null || INSTALL_JAR_MAVEN_VERSION == null)
            {
                handleMissingRequiredInfo();
                return;
            }

            // check if the recommended settings are missing
            if (SENZING_LIBRARY_FILE == null) {
                out.println();
                out.println(
                    "WARNING: The " + LIBRARY_FILE_NAME + " was not found in the " 
                        + LIBRARY_PATH_ENV_VARIABLE);
                out.println(
                    "The install path was inferred Senzing SDK jar in the class path.");
            }

            File repositoryPath = null;
            Set<String> specifiedArgs = new TreeSet<>();
            boolean execute = false;
            for (int index = 0; index < args.length; index++) {
                String arg = args[index];
                if (specifiedArgs.contains(arg)) {
                        err.println();
                        err.println("The " + arg + " cannot be specified more than once.");
                        err.println();
                        exit(1);
                        return;
                }
                specifiedArgs.add(arg);
                switch (arg) {
                    case "-h", "--help", "-d":
                        err.println();
                        err.println("The " + arg + " option must be specified as the only option.");
                        err.println();
                        exit(1);
                        return;
                    case "-r":
                        {
                            int next = ++index;
                            if (next >= args.length) {
                                err.println();
                                err.println("A valid directory path must be specified with the " + arg + " option");
                                err.println();
                                exit(1);
                                return;
                            }
                            String path = args[next];
                            File file = new File(path);
                            if (file.exists() && !file.isDirectory()) {
                                err.println();
                                err.println("If the specified path already exists it must be a directory: " + path);
                                err.println();
                                exit(1);
                                return;
                            }
                            repositoryPath = file;
                        }
                        break;
                    case "-x":
                        if (mvnFile == null) {
                            err.println();
                            err.println("Cannot specify the " + arg + " option if Apache Maven is not found.");
                            err.println();
                            exit(1);
                            return;
                        }
                        execute = true;
                        break;
                    default:
                        err.println();
                        err.println("Unrecognized option: " + arg);
                        err.println();
                        exit(1);
                        return;
                }
            }

            File sourcesJar = findSourcesJarFile();
            File javadocJar = findJavadocJarFile();

            String mvn = (mvnFile == null) ? DEFAULT_MAVEN : mvnFile.getPath();
            out.println();
            out.println(
                "The following command will install sz-sdk.jar in your local Maven repository:");
            out.println();
            out.println(
                quote(mvn) + " install:install-file"
                + " -Dfile=" + quote(INSTALL_JAR_FILE.getPath())
                + " -DgroupId=" + INSTALL_JAR_MAVEN_GROUP_ID
                + " -DartifactId=" + INSTALL_JAR_MAVEN_ARTIFACT_ID
                + " -Dversion=" + INSTALL_JAR_MAVEN_VERSION
                + " -Dpackaging=jar"
                + ((javadocJar != null) ? (" -Djavadoc=" + quote(javadocJar.getPath())) : "")
                + ((sourcesJar != null) ? (" -Dsources=" + quote(sourcesJar.getPath())) : "")
                + ((repositoryPath != null)
                    ? (" -DlocalRepositoryPath=" + quote(repositoryPath.getPath())) : ""));

            out.println();

            // check if executing
            if (execute) {
                String[] cmdArray = buildCommandArray(
                    mvn, javadocJar, sourcesJar, repositoryPath);

                executeMaven(cmdArray);
            }

        } catch (Exception e) {
            err.println(e.getMessage());
            //e.printStackTrace(err);
        }
    }
}
