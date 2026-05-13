package tn.esprit.utils;

import java.io.File;

/**
 * Configuration class for handling product and user image uploads.
 * Manages the shared upload directory for images across the application.
 */
public class ImageConfig {

    /**
     * The absolute path to the uploads directory
     */
    public static final String UPLOAD_DIR = "uploads/posts";

    /**
     * Get the File object for a stored image filename
     * @param filename The filename stored in the database
     * @return The File object pointing to the image, or null if not found
     */
    public static File getImageFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }

        File uploadDir = new File(UPLOAD_DIR);

        // Ensure the upload directory exists
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File imageFile = new File(uploadDir, filename);
        return imageFile;
    }

    /**
     * Get the full URL path for an image (for JavaFX Image loading)
     * @param filename The filename stored in the database
     * @return The file URI string, or null if file doesn't exist
     */
    public static String getImagePath(String filename) {
        File imageFile = getImageFile(filename);
        if (imageFile != null && imageFile.exists()) {
            return imageFile.toURI().toString();
        }
        return null;
    }

    /**
     * Verify that the upload directory exists, create if necessary
     */
    public static void ensureUploadDirExists() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }
}

