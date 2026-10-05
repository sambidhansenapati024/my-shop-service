package com.myShop.my_shop_service.service.inventory;


import com.myShop.my_shop_service.dto.admin.ProductBatchRequest;
import com.myShop.my_shop_service.dto.admin.ProductBatchResponse;
import com.myShop.my_shop_service.dto.admin.ProductRequest;
import com.myShop.my_shop_service.dto.admin.ProductResponse;

import java.util.List;

public interface InventoryService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProduct(Long productId);

    List<ProductResponse> getAllProducts();

    ProductResponse findProductByBarcode(String barcode);

    ProductBatchResponse addBatch(
            Long productId,
            ProductBatchRequest request
    );

    List<ProductBatchResponse> getProductBatches(Long productId);
}