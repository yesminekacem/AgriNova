package tn.esprit.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Central configuration for product image storage.
 *
 * Both the Java (JavaFX) app and the Symfony web app share the same
 * upload directory:
 *   <repo_root>/symf/AgriNovaSYMFONY/public/uploads/products/
 *
 * The database stores the FULL ABSOLUTE PATH to each image file
 * (e.g. C:/Users/.../agrinova/symf/AgriNovaSYMFONY/public/uploads/products/abc.jpg).
 *
 * Java loads images directly from that absolute path.
 * Symfony extracts just the filename from the absolute path and builds
 * the /uploads/products/<filename> web URL for the browser.
 */
public class ImageConfig {

    /**
     * Absolute path to the shared Symfony upload directory.
     * Resolved at startup relative to the Java project root so it works
     * on any machine, as long as java/ and symf/ are siblings inside
     * the same parent folder (agrinova/).
     */
    public static final String UPLOAD_DIR;

    static {
        // user.dir is the Java project root (the folder containing pom.xml / the java/ subproject)
        String javaProjectRoot = System.getProperty("user.dir");
        Path uploadsPath = Paths.get(javaProjectRoot)
                .getParent()                       // agrinova/
                .resolve("symf")
                .resolve("AgriNovaSYMFONY")
                .resolve("public")
                .resolve("uploads")
                .resolve("products")
                .toAbsolutePath();

        UPLOAD_DIR = uploadsPath.toString();

        // Create the directory if it doesn't exist yet
        File dir = uploadsPath.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Returns a File for the given absolute path stored in the database.
     * Falls back gracefully if the value is null or blank.
     */
    public static File getImageFile(String absolutePath) {
        if (absolutePath == null || absolutePath.isBlank()) return null;
        return new File(absolutePath);
    }

    /**
     * Convenience: returns the JavaFX-compatible URI string for an absolute path,
     * or null if the file does not exist.
     */
    public static String getImageUri(String absolutePath) {
        File f = getImageFile(absolutePath);
        if (f != null && f.exists()) {
            return f.toURI().toString();
        }
        return null;
    }
}
