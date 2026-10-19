package org.individualproject.semester3project.infrastructure.media;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.individualproject.semester3project.usecase.media.FileStorageService;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@Service
public class MinioFileStorageService implements FileStorageService {

    private final MinioClient minioClient;
    private final String minioPublicUrl = "http://localhost:9100";

    public MinioFileStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String upload(
            InputStream inputStream,
            long size,
            String contentType,
            Bucket bucket,
            String objectName
    ) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket.getName())
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );

            return minioPublicUrl + "/" + bucket.getName() + "/" + objectName;
        } catch (Exception e) {
            throw new RuntimeException("Upload failed", e);
        }
    }
}