package com.nelly.application.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;
//    public String bucket;
//    public String envDirectory;

    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String DIRECTORY_SEPARATOR = "/";
    private static final String TIME_SEPARATOR = "_";
    private static final int UNDER_BAR_INDEX = 1;

    public String upload(String bucket, MultipartFile multipartFile, String dirName) throws IOException {
        String fileName = buildFileName(multipartFile.getOriginalFilename());
        String fileFormatName = Objects.requireNonNull(multipartFile.getContentType()).substring(multipartFile.getContentType().lastIndexOf("/") + 1);
        String path = dirName + DIRECTORY_SEPARATOR + fileName;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        try (InputStream inputStream = multipartFile.getInputStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, byteArrayInputStream, objectMetadata);
            amazonS3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException("업로드 실패");
        }

        return path;

        // thumbnail image
        /*
        MultipartFile resizedFile = resizeImage(fileName, fileFormatName, multipartFile, 900);
        String thumbFileName = fileName + "/900";
        String thumbPath = dirName + DIRECTORY_SEPARATOR + thumbFileName;
        try (InputStream inputStream = resizedFile.getInputStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, thumbPath, byteArrayInputStream, objectMetadata);
            amazonS3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException("업로드 실패");
        }
        return thumbPath;
       */
    }

    public static String buildFileName(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String uuid = UUID.randomUUID().toString();
        return uuid + fileExtension;
    }

    /* 다운로드 시 사용 */
    public static ContentDisposition createContentDisposition(String categoryWithFileName) {
        String fileName = categoryWithFileName.substring(
                categoryWithFileName.lastIndexOf(DIRECTORY_SEPARATOR) + UNDER_BAR_INDEX);
        return ContentDisposition.builder("attachment")
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
    }

    private MultipartFile resizeImage(String fileName, String fileFormatName, MultipartFile originalImage, int targetWidth) {
        try {
            // MultipartFile -> BufferedImage Convert
            int orientation = 1;
            BufferedImage image = ImageIO.read(originalImage.getInputStream());
            BufferedInputStream inputStream =  new BufferedInputStream(originalImage.getInputStream());
//            Metadata metadata = ImageMetadataReader.readMetadata(inputStream, true);
            // meta2

//            ExifIFD0Directory exifIFD0 = metadata.getDirectory(ExifIFD0Directory.class);
//            if (exifIFD0 != null) {
//                orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION);
//            }

            // 세로 이미지 회전
            switch (orientation) {
                case 1: // [Exif IFD0] Orientation - Top, left side (Horizontal / normal)
                    break;
                case 6: // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
                    image = rotate(image, 90);
                case 3: // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
//                    return Rotation.CW_180;
                    break;
                case 8: // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
//                    return Rotation.CW_270;
                    break;
            }

            // newWidth : newHeight = originWidth : originHeight
            int originWidth = image.getWidth();
            int originHeight = image.getHeight();


            // origin 이미지가 resizing될 사이즈보다 작을 경우 resizing 작업 안 함
            if(originWidth < targetWidth)
                return originalImage;

            MarvinImage imageMarvin = new MarvinImage(image);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth", targetWidth);
            scale.setAttribute("newHeight", targetWidth * originHeight / originWidth);
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

            BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imageNoAlpha, fileFormatName, baos);
            baos.flush();

            return new MockMultipartFile(fileName, baos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 리사이즈에 실패했습니다.");
        }
    }

    private BufferedImage rotate(BufferedImage bi, int degree) {
        int width = bi.getWidth();
        int height = bi.getHeight();

        BufferedImage biFlip;
        if (degree == 90 || degree == 270)
            biFlip = new BufferedImage(height, width, bi.getType());
        else if (degree == 180)
            biFlip = new BufferedImage(width, height, bi.getType());
        else
            return bi;

        if (degree == 90) {
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                    biFlip.setRGB(height - j - 1, i, bi.getRGB(i, j));
        }

        if (degree == 180) {
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                    biFlip.setRGB(width - i - 1, height - j - 1, bi.getRGB(i, j));
        }

        if (degree == 270) {
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                    biFlip.setRGB(j, width - i - 1, bi.getRGB(i, j));
        }

        bi.flush();
        bi = null;
        return biFlip;
    }

    /* remove */
    public void removeObject(String key) {
//        amazonS3Client.deleteObject(new
//        DeleteObjectRequest(bucket, key));
    }
}