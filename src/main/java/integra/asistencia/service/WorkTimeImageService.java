package integra.asistencia.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Service
public class WorkTimeImageService {

    private static final DateTimeFormatter FILENAME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String IMAGE_FORMAT = "jpg";
    private static final float COMPRESSION_QUALITY = 0.4f;

    private Path directorioFotos;

    @PostConstruct
    public void init() throws IOException {
        directorioFotos = Paths.get(System.getProperty("user.dir"), "img");
        Files.createDirectories(directorioFotos);

        log.info("""
                        ========================================
                        📂 INICIALIZANDO DIRECTORIO DE IMÁGENES RELOJ CHECADOR
                        ========================================
                        📁 Directorio: {}
                        Directorio existe: {}
                        Se puede escribir: {}
                        ========================================""",
                directorioFotos.toAbsolutePath(),
                Files.exists(directorioFotos),
                Files.isWritable(directorioFotos)
        );
    }

    /**
     * Guarda la imagen en disco y retorna el nombre del archivo.
     */
    public String saveImg(String data, Integer idEmpleado) throws IOException {
        validateInput(data, idEmpleado);

        String cleanData = cleanBase64Data(data);
        String filename = generateFilename(idEmpleado);

        byte[] imageBytes = Base64.getDecoder().decode(cleanData);
        Path filePath = directorioFotos.resolve(filename);

        Files.write(filePath, imageBytes);
        log.info("📄 Imagen guardada. Archivo: {} en ruta: {}", filename, filePath.toAbsolutePath());

        return filename;
    }

    public Resource getImg(String filename) {
        log.info("🔍 Consultando la imagen: {}", filename);
        Path filePath = getVerifiedFilePath(filename);

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                handleFileNotFound(filename);
            }
            log.info("📄 Imagen encontrada, retornando recurso");
            return resource;
        } catch (Exception e) {
            log.error("Error obteniendo imagen: {}", e.getMessage());
            throw new RuntimeException("Error obteniendo imagen: " + filename, e);
        }
    }

    public Resource getResizedImg(String filename, int width, int height) throws IOException {
        log.info("🔍 Redimensionando imagen: {} a {}x{}", filename, width, height);

        Path filePath = getVerifiedFilePath(filename);
        BufferedImage originalImage = ImageIO.read(filePath.toFile());

        if (originalImage == null) {
            throw new IOException("No se pudo leer la imagen: " + filename);
        }

        BufferedImage resizedImage = resizeImage(originalImage, width, height);
        byte[] compressedImage = compressImage(resizedImage);

        log.info("✅ Imagen redimensionada exitosamente");
        return createResource(compressedImage, filename);
    }

    // ========== MÉTODOS PRIVADOS ==========

    private void validateInput(String data, Integer idEmpleado) {
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("Los datos de la imagen no pueden estar vacíos");
        }
        if (idEmpleado == null || idEmpleado <= 0) {
            throw new IllegalArgumentException("ID de empleado inválido: " + idEmpleado);
        }
    }

    private String cleanBase64Data(String data) {
        if (data.contains(",")) {
            return data.substring(data.indexOf(",") + 1);
        }
        return data;
    }

    private String generateFilename(Integer idEmpleado) {
        String timestamp = LocalDateTime.now().format(FILENAME_FORMATTER);
        return String.format("%d-%s.%s", idEmpleado, timestamp, IMAGE_FORMAT);
    }

    private Path getVerifiedFilePath(String filename) {
        Objects.requireNonNull(filename, "El nombre de archivo no puede ser nulo");

        Path filePath = directorioFotos.resolve(filename).normalize();
        log.info("📂 Ruta completa: {}", filePath.toAbsolutePath());

        if (!Files.exists(filePath)) {
            handleFileNotFound(filename);
        }
        return filePath;
    }

    private void handleFileNotFound(String filename) {
        log.error("❌ Archivo no encontrado: {}", filename);
        logDirectoryContents();
        throw new RuntimeException("Archivo no encontrado: " + filename);
    }

    private void logDirectoryContents() {
        try {
            log.error("📁 Archivos disponibles en {}:", directorioFotos);
            long fileCount = Files.list(directorioFotos)
                    .peek(path -> log.error("  - {}", path.getFileName()))
                    .count();
            if (fileCount == 0) {
                log.error("  (directorio vacío)");
            }
        } catch (IOException e) {
            log.error("Error listando directorio", e);
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image resized = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = outputImage.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.drawImage(resized, 0, 0, null);
        } finally {
            g2d.dispose();
        }
        return outputImage;
    }

    private byte[] compressImage(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {

            ImageWriter writer = ImageIO.getImageWritersByFormatName(IMAGE_FORMAT).next();
            try {
                writer.setOutput(ios);

                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(COMPRESSION_QUALITY);

                writer.write(null, new IIOImage(image, null, null), param);
            } finally {
                writer.dispose();
            }

            return baos.toByteArray();
        }
    }

    private Resource createResource(byte[] imageData, String filename) {
        return new ByteArrayResource(imageData) {
            @Override
            public String getFilename() {
                return filename;
            }
        };
    }
}