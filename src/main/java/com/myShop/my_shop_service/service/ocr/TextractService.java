package com.myShop.my_shop_service.service.ocr;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class TextractService {

    private final TextractClient textractClient;

    public TextractService(
            TextractClient textractClient
    ) {
        this.textractClient = textractClient;
    }

    public String extractText(
            MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Image file is required"
            );
        }

        try {

            byte[] imageBytes =
                    file.getBytes();

            DetectDocumentTextRequest request =
                    DetectDocumentTextRequest.builder()
                            .document(
                                    software.amazon.awssdk.services.textract.model.Document
                                            .builder()
                                            .bytes(
                                                    SdkBytes.fromByteArray(
                                                            imageBytes
                                                    )
                                            )
                                            .build()
                            )
                            .build();

            DetectDocumentTextResponse response =
                    textractClient.detectDocumentText(
                            request
                    );

            return response.blocks()
                    .stream()
                    .filter(block ->
                            block.blockTypeAsString()
                                    .equals("LINE")
                    )
                    .map(Block::text)
                    .collect(
                            Collectors.joining("\n")
                    );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to read uploaded image",
                    e
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to extract text using AWS Textract",
                    e
            );
        }
    }
}