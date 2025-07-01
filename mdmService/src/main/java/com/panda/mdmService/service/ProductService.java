package com.panda.mdmService.service;

import com.panda.mdmService.dto.ProductCreateRequest;
import com.panda.mdmService.dto.ProductDto;
import com.panda.mdmService.dto.ProductUpdateRequest;
import com.panda.mdmService.model.Product;
import com.panda.mdmService.repository.ProductRepository;
import com.panda.mdmService.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Flux<ProductDto> getAll() {
        return productRepository.findAll().map(ProductMapper::toDto);
    }

    public Mono<ProductDto> getById(UUID id) {
        return productRepository.findById(id).map(ProductMapper::toDto);
    }

    public Mono<ProductDto> create(ProductCreateRequest req) {
        Product product = ProductMapper.fromCreateRequest(req);
        product.setId(UUID.randomUUID());
        return productRepository.save(product).map(ProductMapper::toDto);
    }

    public Mono<ProductDto> update(UUID id, ProductUpdateRequest req) {
        return productRepository.findById(id)
                .flatMap(product -> {
                    ProductMapper.updateFromRequest(product, req);
                    return productRepository.save(product);
                })
                .map(ProductMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        return productRepository.deleteById(id);
    }
} 