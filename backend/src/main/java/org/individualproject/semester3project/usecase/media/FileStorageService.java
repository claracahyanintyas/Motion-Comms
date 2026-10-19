package org.individualproject.semester3project.usecase.media;

import org.individualproject.semester3project.infrastructure.media.Bucket;

import java.io.InputStream;

public interface FileStorageService {

    String upload(
            InputStream inputStream,
            long size,
            String contentType,
            Bucket bucket,
            String objectName
    );
}