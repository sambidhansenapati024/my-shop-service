package com.myShop.my_shop_service.controller;

import com.myShop.my_shop_service.dto.ocr.DetectedItem;
import com.myShop.my_shop_service.service.ocr.GroceryItemParserService;
import com.myShop.my_shop_service.service.ocr.TextractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    private final TextractService textractService;
    private final GroceryItemParserService groceryItemParserService;

    public OcrController(
            TextractService textractService,
            GroceryItemParserService groceryItemParserService
    ) {
        this.textractService = textractService;
        this.groceryItemParserService =
                groceryItemParserService;
    }

    @PostMapping(
            value = "/scan",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<?> scanImage(
            @RequestParam("image") MultipartFile image
    ) {

        // 1. Extract raw text from image
        String extractedText =
                textractService.extractText(image);

        // 2. Convert OCR text into grocery items
        List<DetectedItem> detectedItems =
                groceryItemParserService.parse(
                        extractedText
                );

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "text", extractedText,
                        "items", detectedItems
                )
        );
    }
}