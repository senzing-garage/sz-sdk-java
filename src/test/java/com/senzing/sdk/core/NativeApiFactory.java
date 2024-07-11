package com.senzing.sdk.core;

import com.senzing.nativeapi.InstallLocations;
import com.senzing.nativeapi.InvalidInstallationException;
import com.senzing.util.AccessToken;

import java.util.Objects;

/**
 * Provides an abstraction for creating instances of the raw Senzing API.
 * This abstraction allows for alternate implementations to be used especially
 * during auto tests.
 *
 */
public class NativeApiFactory {
  /**
   * The current {@link AccessToken} required to authorize uninstalling the
   * {@link NativeApiProvider}, or <tt>null</tt> if no provider is installed.
   */
  private static AccessToken current_token = null;

  /**
   * The currently installed {@link NativeApiProvider}, or <tt>null</tt> if no
   * provider is installed.
   */
  private static NativeApiProvider api_provider = null;

  /**
   * The {@link InstallLocations} describing the installation directories.
   */
  private static InstallLocations INSTALL_LOCATIONS = null;

  /**
   * Gets the install locations.
   */
  private static synchronized InstallLocations getInstallLocations() {
    if (INSTALL_LOCATIONS == null) {
      INSTALL_LOCATIONS = InstallLocations.findLocations();
    }
    return INSTALL_LOCATIONS;
  }

  /**
   * Installs the {@link NativeApiProvider} to be used by the factory.  If
   * none is installed then the default mechanism of constructing new
   * raw API objects is used.  This returns an {@link AccessToken} to be
   * used for uninstalling the provider later.
   *
   * @param provider The non-null {@link NativeApiProvider} to install.
   *
   * @return The {@link AccessToken} to be used for uninstalling the provider.
   *
   * @throws NullPointerException If the specified provider is <tt>null</tt>.
   *
   * @throws IllegalStateException If a provider is already installed and must
   *                               first be uninstalled.
   */
  public synchronized static AccessToken installProvider(NativeApiProvider provider) {
    Objects.requireNonNull(provider, "The specified provider cannot be null.");
    if (current_token != null) {
      throw new IllegalStateException(
          "A provider is already installed and must first be uninstalled.");
    }
    api_provider  = provider;
    current_token = new AccessToken();
    return current_token;
  }

  /**
   * Checks if a {@link NativeApiProvider} has been installed.
   *
   * @return <tt>true</tt> if a {@link NativeApiProvider} has been installed,
   *         otherwise <tt>false</tt>.
   */
  public synchronized static boolean isProviderInstalled() {
    return (current_token != null);
  }

  /**
   * Uninstalls a previously installed {@link NativeApiProvider} using the
   * specified {@link AccessToken} to authorize the operation.  If no provider
   * has been installed then this method does nothing.
   *
   * @param token The {@link AccessToken} that was returned when installing the
   *              provider.
   *
   * @throws IllegalArgumentException If the specified token does not match the
   *                                  token that was returned when the provider
   *                                  was installed.
   */
  @SuppressWarnings("unused")
  private synchronized static void uninstallProvider(AccessToken token) {
    if (current_token == null) {
      return;
    }
    if (!current_token.equals(token)) {
      throw new IllegalArgumentException(
          "The specified access token is not the expected access token to "
          + "authorize unintalling the provider.");
    }
    current_token = null;
    api_provider  = null;
  }

  /***
   * Internal method for getting the currently installed provider in a
   * thread-safe manner.
   *
   * @return The currently installed provider, or <tt>null</tt> if no provider
   *         is currently installed.
   */
  private static NativeApiProvider getInstalledProvider() {
    return api_provider;
  }

  /**
   * Creates a new instance of {@link NativeEngine} to use.  If a
   * {@link NativeApiProvider} is installed then it is used to create
   * the instance, otherwise a new instance of {@link NativeEngineJni} is
   * constructed and returned.
   *
   * @return A new instance of {@link NativeEngine} to use.
   */
  public static NativeEngine createEngineApi() {
    NativeApiProvider provider = getInstalledProvider();
    if (provider != null) {
      return provider.createEngineApi();

    } else if (getInstallLocations() == null) {
      throw new InvalidInstallationException(
          "Unable to find Senzing native installation.");

    } else {
      return new NativeEngineJni();
    }
  }

  /**
   * Provides a new instance of {@link NativeConfig} to use.  If a
   * {@link NativeApiProvider} is installed then it is used to create
   * the instance, otherwise a new instance of {@link NativeConfigJni} is
   * constructed and returned.
   *
   * @return A new instance of {@link NativeConfig} to use.
   */
  public static NativeConfig createConfigApi() {
    NativeApiProvider provider = getInstalledProvider();
    if (provider != null) {
      return provider.createConfigApi();

    } else if (getInstallLocations() == null) {
      throw new InvalidInstallationException(
          "Unable to find Senzing native installation.");

    } else {
      return new NativeConfigJni();
    }
  }

  /**
   * Provides a new instance of {@link NativeProduct} to use.  If a
   * {@link NativeApiProvider} is installed then it is used to create
   * the instance, otherwise a new instance of {@link NativeProductJni} is
   * constructed and returned.
   *
   * @return A new instance of {@link NativeProduct} to use.
   */
  public static NativeProduct createProductApi() {
    NativeApiProvider provider = getInstalledProvider();
    if (provider != null) {
      return provider.createProductApi();

    } else if (getInstallLocations() == null) {
      throw new InvalidInstallationException(
          "Unable to find Senzing native installation.");

    } else {
      return new NativeProductJni();
    }
  }

  /**
   * Provides a new instance of {@link NativeConfigManager} to use.  If a
   * {@link NativeApiProvider} is installed then it is used to create
   * the instance, otherwise a new instance of {@link NativeConfigManagerJni} is
   * constructed and returned.
   *
   * @return A new instance of {@link NativeConfigManager} to use.
   *
   */
  public static NativeConfigManager createConfigMgrApi() {
    NativeApiProvider provider = getInstalledProvider();
    if (provider != null) {
      return provider.createConfigMgrApi();

    } else if (getInstallLocations() == null) {
      throw new InvalidInstallationException(
          "Unable to find Senzing native installation.");

    } else {
      return new NativeConfigManagerJni();
    }
  }

  /**
   * Provides a new instance of {@link NativeDiagnostic} to use.  If a
   * {@link NativeApiProvider} is installed then it is used to create
   * the instance, otherwise a new instance of {@link NativeDiagnosticJni} is
   * constructed and returned.
   *
   * @return A new instance of {@link NativeDiagnostic} to use.
   *
   */
  public static NativeDiagnostic createDiagnosticApi() {
    NativeApiProvider provider = getInstalledProvider();
    if (provider != null) {
      return provider.createDiagnosticApi();

    } else if (getInstallLocations() == null) {
      throw new InvalidInstallationException(
          "Unable to find Senzing native installation.");

    } else {
      return new NativeDiagnosticJni();
    }
  }

}
