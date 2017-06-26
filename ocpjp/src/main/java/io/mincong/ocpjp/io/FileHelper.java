package io.mincong.ocpjp.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File helper, copied from book <i>OCP Java SE 7, Programmer II,
 * Mala Gupta</i>, §7.3.3 (page 478).
 * <p>
 * The content is modified.
 *
 * @author Mala Gupta
 */
public final class FileHelper {

  private FileHelper() {
    // Utility class, do not instantiate
  }

  /**
   * Copies file content of the input file to the output file by
   * single byte.
   */
  public static void copyBySingleByte(File inputFile, File outputFile) throws IOException {
    try (
        FileInputStream in = new FileInputStream(inputFile);
        FileOutputStream out = new FileOutputStream(outputFile)
    ) {
      // Declares variable to store a single byte of data
      int b;

      // Loops until end of steam is reached
      // (no more bytes can be read)
      while ((b = in.read()) != -1) {
        // write byte data to destination file
        out.write(b);
      }
    }
  }

  /**
   * Copies file content of the input file to the output file by
   * multi-bytes (1024).
   * <p>
   * I/O operations that require reading and writing of a single byte
   * from and to a file are a costly affair. To optimize these
   * operations, we can use a byte array {@code byte[]}.
   */
  public static void copyByMultiBytes(File inputFile, File outputFile) throws IOException {
    try (
        FileInputStream in = new FileInputStream(inputFile);
        FileOutputStream out = new FileOutputStream(outputFile)
    ) {
      int len;
      byte[] byteArr = new byte[1024];

      // Unlike read(), read(byte[]) doesn't return the read bytes,
      // it returns the length of bytes read, or -1 if no more data
      // can be read. The actual data read is stored in the byteArr
      while ((len = in.read(byteArr)) != -1) {
        // write byte data to destination file
        out.write(byteArr, 0, len);
      }
    }
  }

}
