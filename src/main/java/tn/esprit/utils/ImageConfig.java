package tn.esprit.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Central configuration for product image storage.
 *
 * Both the Java (JavaFX) app and the Symfony web app share the same
 * upload directory:  <project_root>/symf/AgriNovaSYMFONY/public/uploads/products/
 *
 * Java writes images there when a product is created/edited, and reads
 * them from there when displaying product cards.  Symfony already writes
 * and reads from the same location, so both sides will always see the
 * same files.
 */
public class ImageConfig {

    /**
     * Absolute path to the shared Symfony upload directory.
     *
     * The path is resolved relative to the Java project root so it works
     * regardless of where on disk the repo is cloned, as long as the two
     * sub-projects (java/ and symf/) sit next to each other inside the
     * same parent folder (agrinova/).
     */
    public static final String UPLOAD_DIR;

    static {
        // Walk up from java/ to agrinova/, then down to the Symfony public dir
        String javaProjectRoot = System.getProperty("user.dir"); // e.g. .../agrinova/java
        Path uploadsPath = Paths.get(javaProjectRoot)
                .getParent()                          // agrinova/
                .resolve("symf")
                .resolve("AgriNovaSYMFONY")
                .resolve("public")
                .resolve("uploads")
                .resolve("products");

        UPLOAD_DIR = uploadsPath.toAbsolutePath().toString();

        // Create the directory if it doesn't exist yet
        File dir = uploadsPath.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /** Returns the full path to a stored image given just its filename. */
    public static File getImageFile(String filename) {
        if (filename == null || filename.isBlank()) return null;
        return new File(UPLOAD_DIR, filename);
    }
}
