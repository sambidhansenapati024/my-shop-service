package com.myShop.my_shop_service.service.inventory;

import com.myShop.my_shop_service.dto.admin.ProductBatchRequest;
import com.myShop.my_shop_service.dto.admin.ProductBatchResponse;
import com.myShop.my_shop_service.dto.admin.ProductRequest;
import com.myShop.my_shop_service.dto.admin.ProductResponse;
import com.myShop.my_shop_service.entity.Product;
import com.myShop.my_shop_service.entity.ProductBatch;
import com.myShop.my_shop_service.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final com.myShop.my_shop_service.repository.ProductBatchRepository productBatchRepository;

    public InventoryServiceImpl(
            ProductRepository productRepository,
            com.myShop.my_shop_service.repository.ProductBatchRepository productBatchRepository
    ) {
        this.productRepository = productRepository;
        this.productBatchRepository = productBatchRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        String barcode = normalize(request.getBarcode());

        if (barcode != null
                && productRepository.existsByBarcode(barcode)) {
            throw new IllegalArgumentException(
                    "A product with this barcode already exists"
            );
        }

        Product product = new Product();

        product.setProductName(
                request.getProductName().trim()
        );

        product.setBarcode(barcode);

        product.setUnit(
                request.getUnit().trim()
        );

        Product savedProduct =
                productRepository.save(product);

        return toProductResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long productId) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found"
                        )
                );

        return toProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::toProductResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findProductByBarcode(
            String barcode
    ) {

        String normalizedBarcode =
                normalize(barcode);

        if (normalizedBarcode == null) {
            throw new IllegalArgumentException(
                    "Barcode is required"
            );
        }

        Product product = productRepository
                .findByBarcode(normalizedBarcode)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found for barcode: "
                                        + normalizedBarcode
                        )
                );

        return toProductResponse(product);
    }

    @Override
    public ProductBatchResponse addBatch(
            Long productId,
            ProductBatchRequest request
    ) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found"
                        )
                );

        String batchNumber =
                request.getBatchNumber().trim();

        if (productBatchRepository
                .findByProductIdAndBatchNumber(
                        productId,
                        batchNumber
                )
                .isPresent()) {

            throw new IllegalArgumentException(
                    "This batch already exists for the product"
            );
        }

        ProductBatch batch = new ProductBatch();

        batch.setProduct(product);

        batch.setBatchNumber(batchNumber);

        batch.setPurchasePrice(
                request.getPurchasePrice()
        );

        batch.setSellingPrice(
                request.getSellingPrice()
        );

        batch.setQuantity(
                request.getQuantity()
        );

        batch.setExpiryDate(
                request.getExpiryDate()
        );

        ProductBatch savedBatch =
                productBatchRepository.save(batch);

        return toProductBatchResponse(savedBatch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductBatchResponse> getProductBatches(
            Long productId
    ) {

        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException(
                    "Product not found"
            );
        }

        return productBatchRepository
                .findByProductId(productId)
                .stream()
                .map(this::toProductBatchResponse)
                .toList();
    }

    private ProductResponse toProductResponse(
            Product product
    ) {

        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getBarcode(),
                product.getUnit()
        );
    }

    private ProductBatchResponse toProductBatchResponse(
            ProductBatch batch
    ) {

        return new ProductBatchResponse(
                batch.getId(),
                batch.getProduct().getId(),
                batch.getBatchNumber(),
                batch.getPurchasePrice(),
                batch.getSellingPrice(),
                batch.getQuantity(),
                batch.getExpiryDate()
        );
    }

    private String normalize(String value) {

        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty()
                ? null
                : trimmed;
    }
}