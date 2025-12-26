package ru.itis.marketplace.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itis.marketplace.config.AppProperties;
import ru.itis.marketplace.exceptions.ValidationException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;

/*
 *   Пока что работа с MIME типами и сохранение происходит здесь
 *   В дальнейшем планирую сделать сервис и миграцию в бд для работы с файлами
 */
public class ServletFileUtil {
    private static final String UPLOAD_DIRECTORY =
            AppProperties.getProperty("file.upload.directory", "uploads");
    private static final Logger logger = LoggerFactory.getLogger(ServletFileUtil.class);
    private static final Map<String, String> ALLOWED_MIME_TYPES = Map.of(
            "image/jpeg", ".jpg",
            "image/jpg", ".jpg",
            "image/png", ".png",
            "image/gif", ".gif",
            "image/webp", ".webp"
    );

    public static void saveEntityImages(Class<?> entityClass, Long entityId, HttpServletRequest req)
            throws IOException, ValidationException, ServletException {
        logger.debug("Saving entity images for {} {}", entityClass.getSimpleName(), entityId);
        List<Part> imageParts = req.getParts().stream()
                .filter(part -> "images".equals(part.getName()))
                .toList();
        logger.debug("Found {} images", imageParts.size());
        for (int i = 0; i < imageParts.size(); i++) {
            Part imagePart = imageParts.get(i);
            if (imagePart != null && imagePart.getSize() > 0) {
                saveSingleImage(entityClass.getSimpleName().toLowerCase(), entityId, imagePart, i);
            }
        }
    }

    public static List<String> getEntityImages(Class<?> entityClass, Long entityId, HttpServletRequest req) {
        String entityType = entityClass.getSimpleName().toLowerCase();
        logger.debug("Getting all entity images for {} with id {}", entityType, entityId);
        String entityDir = getEntityDirectory(entityType, entityId);

        File directory = new File(entityDir);
        List<String> imagePaths = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles(ServletFileUtil::isSupportedFile);

            if (files != null) {
                Arrays.sort(files, (f1, f2) -> {
                    int index1 = extractImageIndex(f1.getName());
                    int index2 = extractImageIndex(f2.getName());
                    return Integer.compare(index1, index2);
                });

                for (File file : files) {
                    String webPath = getWebPathToFile(req, entityType, entityId, file.getName());
                    imagePaths.add(webPath);
                }
            }
        }

        return imagePaths;
    }

    public static String getEntityMainImage(Class<?> entityClass, Long entityId, HttpServletRequest req) {
        String entityType = entityClass.getSimpleName().toLowerCase();
        logger.debug("Get entity main image for {} {}", entityType, entityId);
        String entityDir = getEntityDirectory(entityType, entityId);

        File directory = new File(entityDir);

        if (!directory.exists() || !directory.isDirectory()) {
            logger.debug("Directory {} does not exist or is not a directory", entityDir);
            return null;
        }

        File[] files = directory.listFiles(ServletFileUtil::isSupportedFile);

        if (files == null || files.length == 0) {
            logger.debug("Directory {} does not contain any files", entityDir);
            return null;
        }
        File mainImageFile = null;
        int minIndex = Integer.MAX_VALUE;

        for (File file : files) {
            int currentIndex = extractImageIndex(file.getName());
            if (currentIndex < minIndex) {
                minIndex = currentIndex;
                mainImageFile = file;
            }
        }
        if (mainImageFile != null) {
            logger.debug("Successfully retrieved main image for {} {}", entityType, entityId);
            return getWebPathToFile(req, entityType, entityId, mainImageFile.getName());
        }
        logger.debug("Directory {} does not contain any files", entityDir);
        return null;
    }

    public static void deleteEntityImages(Class<?> entityClass, Long entityId) {
        String entityType = entityClass.getSimpleName().toLowerCase();
        logger.debug("Deleting entity images for {} {}", entityType, entityId);
        String directory = getEntityDirectory(entityType, entityId);
        try {
            FileUtils.cleanDirectory(new File(directory));
            logger.debug("Successfully deleted entity images for {} {}", entityType, entityId);
        } catch (IOException e) {
            logger.error("Error cleaning up of directory: {}", directory, e);
        }
    }


    private static int extractImageIndex(String fileName) {
        logger.debug("Extracting image index for file: {}", fileName);
        if (fileName.startsWith("img") && fileName.length() > 3) {
            try {
                String numberStr = fileName.substring(3);
                StringBuilder digits = new StringBuilder();
                for (int i = 0; i < numberStr.length(); i++) {
                    char c = numberStr.charAt(i);
                    if (Character.isDigit(c)) {
                        digits.append(c);
                    } else {
                        break;
                    }
                }
                return !digits.isEmpty() ? Integer.parseInt(digits.toString()) : 0;
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private static void saveSingleImage(String entityType, Long entityId,
                                        Part imagePart, int index) throws IOException, ValidationException {
        String mimeType = imagePart.getContentType();
        logger.debug("Saving single image for {} {}", entityType, entityId);
        if (!ALLOWED_MIME_TYPES.containsKey(mimeType)) {
            logger.debug("MIME type {} not supported", mimeType);
            throw new ValidationException("Unsupported MIME type: " + mimeType);
        }

        String uploadDir = getEntityDirectory(entityType, entityId);

        File entityDir = new File(uploadDir);
        if (!entityDir.exists()) {
            if (!entityDir.mkdirs()) {
                logger.error("Error creating directory: {}", uploadDir);
                throw new IOException("Unable to create directory: %s".formatted(uploadDir));
            }
        }
        String extension = ALLOWED_MIME_TYPES.get(mimeType);
        String fileName = getFileName(entityType, entityId, index, extension);
        String filePath = "%s/%s".formatted(uploadDir, fileName);
        logger.debug("Trying to save image {} {}", filePath, fileName);
        imagePart.write(filePath);
        logger.debug("Successfully saved image {} {}", filePath, fileName);

    }

    private static boolean isSupportedFile(File file) {
        if (file == null || !file.isFile()) {
            return false;
        }

        String fileName = file.getName();
        if (!fileName.startsWith("img")) {
            return false;
        }
        String extension = getFileExtension(fileName);

        return ALLOWED_MIME_TYPES.containsValue(extension);
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex);
    }

    private static String getFileName(String entityType, Long entityId, int index, String extension) {
        return "img%d_%s%s%s"
                .formatted(index, entityType, entityId, extension);
    }

    private static String getEntityDirectory(String entityType, Long entityId) {
        return "%s/%s/%s"
                .formatted(UPLOAD_DIRECTORY, entityType, entityId);
    }

    private static String getWebPathToFile(HttpServletRequest req, String entityType, Long entityId, String fileName) {
        return "%s/%s/%s/%s/%s"
                .formatted(
                        req.getContextPath(),
                        "uploads",
                        entityType,
                        entityId,
                        fileName
                );
    }
}