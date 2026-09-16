package com.myShop.my_shop_service.service.s3Storage;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3StorageService {

    String uploadFile(MultipartFile file, String folder) throws IOException;

    void deleteFile(String key);

    String generatePresignedUrl(String key);
}
