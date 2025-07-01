package com.panda.mdmService.controller;

import com.panda.mdmService.dto.ProductCreateRequest;
import com.panda.mdmService.dto.ProductDto;
import com.panda.mdmService.dto.ProductUpdateRequest;
import com.panda.mdmService.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Flux<ProductDto> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getById(@PathVariable UUID id) {
        return productService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductDto> create(@RequestBody ProductCreateRequest req) {
        return productService.create(req);
    }

    @PutMapping("/{id}")
    public Mono<ProductDto> update(@PathVariable UUID id, @RequestBody ProductUpdateRequest req) {
        return productService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return productService.delete(id);
    }
} 