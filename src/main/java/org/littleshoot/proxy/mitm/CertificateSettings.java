package org.littleshoot.proxy.mitm;

import com.google.common.base.Preconditions;

public class CertificateSettings {
  public static final int DEFAULT_ROOT_KEY_SIZE = 2048;
  public static final int DEFAULT_FAKE_KEY_SIZE = 1024;

  public static final int MIN_KEY_SIZE = 1024;
  public static final int MAX_KEY_SIZE = 8192;

  private final int defaultRootKeySize;
  private final int fakeKeySize;

  /**
   * @param defaultRootKeySize
   *        RSA key size used to generate fallback root certificate.
   *        If there's a root authority file found on system this parameter is ignored.
   * @param fakeKeySize
   *        RSA key size for 'fake' x509 used to impersonate upstreams.
   */
  private CertificateSettings(int defaultRootKeySize, int fakeKeySize) {
    this.defaultRootKeySize = defaultRootKeySize;
    this.fakeKeySize = fakeKeySize;
  }

  public int getDefaultRootKeySize() {
    return defaultRootKeySize;
  }

  public int getFakeKeySize() {
    return fakeKeySize;
  }

  public static CertificateSettingsBuilder builder() {
    return new CertificateSettingsBuilder();
  }

  public static class CertificateSettingsBuilder {
    private int defaultRootKeySize = DEFAULT_ROOT_KEY_SIZE;
    private int fakeKeySize = DEFAULT_FAKE_KEY_SIZE;

    public CertificateSettingsBuilder setDefaultRootKeySize(int defaultRootKeySize) {
      checkKeySize(defaultRootKeySize);
      this.defaultRootKeySize = defaultRootKeySize;
      return this;
    }

    public CertificateSettingsBuilder setFakeKeySize(int defaultFakeKeySize) {
      checkKeySize(defaultFakeKeySize);
      this.fakeKeySize = defaultFakeKeySize;
      return this;
    }

    public CertificateSettings build() {
      return new CertificateSettings(defaultRootKeySize, fakeKeySize);
    }

    private static void checkKeySize(int keySize) {
      Preconditions.checkArgument(keySize >= MIN_KEY_SIZE, "keySize does not satisfy the min requirements");
      Preconditions.checkArgument(keySize <= MAX_KEY_SIZE, "keySize does not satisfy the max requirements");
      Preconditions.checkArgument(isPowerOf2(keySize), "keySize should be a power of 2");
    }

    private static boolean isPowerOf2(int number) {
      return (number & (number - 1)) == 0;
    }
  }
}
