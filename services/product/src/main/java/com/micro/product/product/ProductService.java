package com.micro.product.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micro.product.exception.ProductPurchaseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(ProductRequest request) {
        Product product = productMapper.toProduct(request);

        return productRepository.save(product).getId();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public List<ProductPurchaseResponse> purchaseProduct(List<ProductPurchaseRequest> request) {
        var productIds = request.stream().map(ProductPurchaseRequest::productId).toList();
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);

        if (storedProducts.size() != productIds.size()) {
            throw new ProductPurchaseException("One or more products not found");
        }
        var storesRequest = request.stream().sorted(Comparator.comparing(ProductPurchaseRequest::productId)).toList();
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for (int i = 0; i < storedProducts.size(); i++) {
            var storedProduct = storedProducts.get(i);
            var requestProduct = storesRequest.get(i);
            if (storedProduct.getAvailableQuantity() < requestProduct.quantity()) {
                throw new ProductPurchaseException("Not enough quantity for product: " + storedProduct.getId());
            }
            var newAvailableQuantity = storedProduct.getAvailableQuantity() - requestProduct.quantity();
            storedProduct.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(storedProduct);
            purchasedProducts.add(productMapper.toproductPurchaseResponse(storedProduct, requestProduct.quantity()));
        }
        return purchasedProducts;
    }

    public ProductResponse getProductById(Integer productId) {
        return productRepository.findById(productId).map(productMapper::toProductResponse).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(productMapper::toProductResponse).toList();
    }

}
