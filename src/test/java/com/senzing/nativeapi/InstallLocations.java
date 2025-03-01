package com.senzing.nativeapi;

import com.senzing.util.JsonUtilities;

import javax.json.JsonObject;
import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static com.senzing.io.IOUtilities.readTextFileAsString;
import static com.senzing.util.OperatingSystemFamily.RUNTIME_OS_FAMILY;

/**
 * Describes the directories on disk used to find the Senzing product
 * installation and the support directories.
 */
public class InstallLocations {
    /**
     * The installation location.
     */
    private File installDir;

    /**
     * The location of the configuration files for the config directory.
     */
    private File configDir;

    /**
     * The location of the resource files for the resource directory.
     */
    private File resourceDir;

    /**
     * The location of the support files for the support directory.
     */
    private File supportDir;

    /**
     * The location of the template files for the template directory.
     */
    private File templatesDir;

    /**
     * Indicates if the installation direction is from a development build.
     */
    private boolean devBuild = false;

    /**
     * Default constructor.
     */
    private InstallLocations() {
        this.installDir = null;
        this.configDir = null;
        this.resourceDir = null;
        this.supportDir = null;
        this.templatesDir = null;
        this.devBuild = false;
    }

    /**
     * Gets the primary installation directory.
     *
     * @return The primary installation directory.
     */
    public File getInstallDirectory() {
        return this.installDir;
    }

    /**
     * Gets the configuration directory.
     *
     * @return The configuration directory.
     */
    public File getConfigDirectory() {
        return this.configDir;
    }

    /**
     * Gets the resource directory.
     *
     * @return The resource directory.
     */
    public File getResourceDirectory() {
        return this.resourceDir;
    }

    /**
     * Gets the support directory.
     *
     * @return The support directory.
     */
    public File getSupportDirectory() {
        return this.supportDir;
    }

    /**
     * Gets the templates directory.
     *
     * @return The templates directory.
     */
    public File getTemplatesDirectory() {
        return this.templatesDir;
    }

    /**
     * Checks if the installation is actually a development build.
     * 
     * @return <code>true</code> if this installation represents a
     *         development build, otherwise <code>false</code>.
     */
    public boolean isDevelopmentBuild() {
        return this.devBuild;
    }

    /**
     * Produces a {@link String} describing this instance.
     * 
     * @return A {@link String} describing this instance.
     */
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.println();
        pw.println("--------------------------------------------------");
        pw.println("installDirectory   : " + this.getInstallDirectory());
        pw.println("configDirectory    : " + this.getConfigDirectory());
        pw.println("supportDirectory   : " + this.getSupportDirectory());
        pw.println("resourceDirectory  : " + this.getResourceDirectory());
        pw.println("templatesDirectory : " + this.getTemplatesDirectory());
        pw.println("developmentBuild   : " + this.isDevelopmentBuild());

        return sw.toString();
    }

    /**
     * Finds the install directories and returns the {@link InstallLocations}
     * instance describing those locations.
     *
     * @return The {@link InstallLocations} instance describing the install
     *         locations.
     */
    public static InstallLocations findLocations() {
        File installDir = null;
        File configDir = null;
        File resourceDir = null;
        File supportDir = null;
        File templatesDir = null;
        try {
            String defaultInstallPath;
            String defaultConfigPath = null;
            String defaultSupportPath = null;

            // check if we are building within the dev structure
            String[] directoryStructure = { "sz-sdk-java", "java", "g2", "apps", "dev" };
            File homeDir        = new File(System.getProperty("user.home"));
            File homeSenzing    = new File(homeDir, "senzing");
            File homeInstall    = new File(homeSenzing, "er");
            File homeSupport    = new File(homeInstall, "data");
            File workingDir     = new File(System.getProperty("user.dir"));
            File previousDir    = null;
            boolean devStructure = true;
            for (String dirName : directoryStructure) {
                if (workingDir == null)
                    break;
                if (!workingDir.getName().equalsIgnoreCase(dirName)) {
                    devStructure = false;
                    break;
                }
                previousDir = workingDir;
                workingDir = workingDir.getParentFile();
            }
            File devDistDir = (devStructure) ? new File(previousDir, "dist") : null;
            File devSupport = (devStructure) ? new File(devDistDir, "data") : null;
            switch (RUNTIME_OS_FAMILY) {
                case WINDOWS:
                    if (devDistDir == null) {
                        defaultInstallPath = homeInstall.getCanonicalPath();
                        defaultSupportPath = homeSupport.getCanonicalPath();
                    } else {
                        defaultInstallPath = devDistDir.getCanonicalPath();
                        defaultSupportPath = devSupport.getCanonicalPath();
                    }
                    break;
                case MAC_OS:
                    if (devDistDir == null) {
                        defaultInstallPath = homeInstall.getCanonicalPath();
                        defaultSupportPath = homeSupport.getCanonicalPath();
                    } else {
                        defaultInstallPath = devDistDir.getCanonicalPath();
                        defaultSupportPath = devSupport.getCanonicalPath();
                    }
                break;
                case UNIX:
                    if (devDistDir == null) {
                        defaultInstallPath  = "/opt/senzing/er";
                        defaultSupportPath  = "/opt/senzing/data";
                        defaultConfigPath   = "/etc/opt/senzing";
                    } else {
                        defaultInstallPath = devDistDir.getCanonicalPath();
                        defaultSupportPath = devSupport.getCanonicalPath();
                    }
                    break;
                default:
                    throw new IllegalStateException(
                            "Unrecognized Operating System: " + RUNTIME_OS_FAMILY);
            }

            // check for senzing system properties
            String installPath = System.getProperty("senzing.install.dir");
            String configPath = System.getProperty("senzing.config.dir");
            String supportPath = System.getProperty("senzing.support.dir");
            String resourcePath = System.getProperty("senzing.resource.dir");

            // try environment variables if system properties don't work
            if (installPath == null || installPath.trim().length() == 0) {
                installPath = System.getenv("SENZING_DIR");
            }
            if (configPath == null || configPath.trim().length() == 0) {
                configPath = System.getenv("SENZING_ETC_DIR");
            }
            if (supportPath == null || supportPath.trim().length() == 0) {
                supportPath = System.getenv("SENZING_DATA_DIR");
            }

            // normalize empty strings as null
            if (installPath != null && installPath.trim().length() == 0) {
                installPath = null;
            }
            if (configPath != null && configPath.trim().length() == 0) {
                configPath = null;
            }
            if (supportPath != null && supportPath.trim().length() == 0) {
                supportPath = null;
            }
            if (resourcePath != null && resourcePath.trim().length() == 0) {
                resourcePath = null;
            }

            // check the senzing directory
            installDir = new File(installPath == null ? defaultInstallPath : installPath);
            if (!installDir.exists()) {
                System.err.println("Could not find Senzing installation directory:");
                System.err.println("     " + installDir);
                System.err.println();
                if (installPath != null) {
                    System.err.println(
                            "Check the -Dsenzing.install.dir=[path] command line option.");
                } else {
                    System.err.println(
                            "Use the -Dsenzing.install.dir=[path] command line option to "
                                    + "specify a path");
                }

                return null;
            }

            // normalize the senzing directory
            String dirName = installDir.getName();
            if (installDir.isDirectory()
                    && !dirName.equalsIgnoreCase("er")
                    && dirName.equalsIgnoreCase("senzing")) {
                // for windows or linux allow the "Senzing" dir as well
                installDir = new File(installDir, "er");
            }

            if (!installDir.isDirectory()) {
                System.err.println("Senzing installation directory appears invalid:");
                System.err.println("     " + installDir);
                System.err.println();
                if (installPath != null) {
                    System.err.println(
                            "Check the -Dsenzing.install.dir=[path] command line option.");
                } else {
                    System.err.println(
                            "Use the -Dsenzing.install.dir=[path] command line option to "
                                    + "specify a path");
                }

                return null;
            }

            // check if an explicit support path has been specified
            if (supportPath == null || supportPath.trim().length() == 0) {
                supportDir = new File(defaultSupportPath);
            } else {
                // use the specified explicit path
                supportDir = new File(supportPath);
            }

            if (!supportDir.exists()) {
                System.err.println("The support directory does not exist:");
                System.err.println("         " + supportDir);
                if (supportPath != null) {
                    System.err.println(
                            "Check the -Dsenzing.support.dir=[path] command line option.");
                } else {
                    System.err.println(
                            "Use the -Dsenzing.support.dir=[path] command line option to "
                                    + "specify a path");
                }

                throw new InvalidInstallationException(
                        "The support directory does not exist: " + supportDir);
            }

            if (!supportDir.isDirectory()) {
                System.err.println("The support directory is invalid:");
                System.err.println("         " + supportDir);
                if (supportPath != null) {
                    System.err.println(
                            "Check the -Dsenzing.support.dir=[path] command line option.");
                } else {
                    System.err.println(
                            "Use the -Dsenzing.support.dir=[path] command line option to "
                                    + "specify a path");
                }
                throw new InvalidInstallationException(
                        "The support directory is invalid: " + supportDir);

            }

            // check the config path
            if (configPath != null) {
                configDir = new File(configPath);
            }

            // check for a dev build installation
            if (configDir == null && installDir != null && "dist".equals(installDir.getName())) {
                configDir = new File(installDir, "data");
            }

            // if still null and there is a default, then use it
            if (configDir == null && defaultConfigPath != null) {
                configDir = new File(defaultConfigPath);
                if (!configDir.exists()) {
                    configDir = null;
                }
            }

            // if still null, try to use the install's etc directory
            if (configDir == null && installDir != null) {
                configDir = new File(installDir, "etc");
                if (!configDir.exists()) {
                    configDir = null;
                }                
            }

            if (configPath != null && configDir == null || (!configDir.exists())) {
                System.err.println(
                        "The -Dsenzing.config.dir=[path] option specifies a path that does not exist:");
                System.err.println(
                        "         " + configPath);

                throw new InvalidInstallationException(
                        "Explicit config path does not exist: " + configPath);
            }
            if (configDir != null && configDir.exists()) {
                if (!configDir.isDirectory()) {
                    System.err.println(
                            "The -Dsenzing.config.dir=[path] option specifies a file, not a directory:");
                    System.err.println(
                            "         " + configPath);

                    throw new InvalidInstallationException(
                            "Explicit config path is not directory: " + configPath);
                }

                String[] requiredFiles = { "cfgVariant.json" };
                List<String> missingFiles = new ArrayList<>(requiredFiles.length);

                for (String fileName : requiredFiles) {
                    File configFile = new File(configDir, fileName);
                    File supportFile = new File(supportDir, fileName);
                    if (!configFile.exists() && !supportFile.exists()) {
                        missingFiles.add(fileName);
                    }
                }
                if (missingFiles.size() > 0 && configPath != null) {
                    System.err.println(
                            "The -Dsenzing.config.dir=[path] option specifies an invalid config directory:");
                    for (String missing : missingFiles) {
                        System.err.println(
                                "         " + missing + " was not found");
                    }
                    throw new InvalidInstallationException(
                            "Explicit config path missing required files: " + missingFiles);
                }
            }

            // now determine the resource path
            resourceDir = (resourcePath == null) ? null : new File(resourcePath);
            if (resourceDir == null) {
                resourceDir = new File(installDir, "resources");
                if (!resourceDir.exists())
                    resourceDir = null;
            }

            if (resourceDir != null && resourceDir.exists()
                    && resourceDir.isDirectory()) {
                templatesDir = new File(resourceDir, "templates");
            }

            if (resourcePath != null) {
                if (!resourceDir.exists()) {
                    System.err.println(
                            "The -Dsenzing.resource.dir=[path] option specifies a path that does not exist:");
                    System.err.println(
                            "         " + resourcePath);

                    throw new InvalidInstallationException(
                            "Explicit resource path does not exist: " + resourcePath);
                }

                if (!resourceDir.isDirectory()
                        || !templatesDir.exists()
                        || !templatesDir.isDirectory()) {
                    System.err.println(
                            "The -Dsenzing.resource.dir=[path] option specifies an invalid "
                                    + "resource directory:");
                    System.err.println("         " + resourcePath);

                    throw new InvalidInstallationException(
                            "Explicit resource path is not valid: " + resourcePath);
                }

            } else if (!resourceDir.exists() || !resourceDir.isDirectory()
                    || !templatesDir.exists() || !templatesDir.isDirectory()) {
                resourceDir = null;
                templatesDir = null;
            }

            // construct and initialize the result
            InstallLocations result = new InstallLocations();
            result.installDir = installDir;
            result.configDir = configDir;
            result.supportDir = supportDir;
            result.resourceDir = resourceDir;
            result.templatesDir = templatesDir;
            result.devBuild = ("dist".equals(installDir.getName()));

            // return the result
            return result;

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
