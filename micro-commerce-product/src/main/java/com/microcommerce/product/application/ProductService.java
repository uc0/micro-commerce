package com.microcommerce.product.application;

import com.microcommerce.product.domain.dao.MemberClientDao;
import com.microcommerce.product.domain.dto.feign.res.ProfileResDto;
import com.microcommerce.product.domain.dto.res.CreateProductResDto;
import com.microcommerce.product.domain.dto.res.PageDto;
import com.microcommerce.product.domain.dto.res.ProductDetailResDto;
import com.microcommerce.product.domain.dto.res.ProductResDto;
import com.microcommerce.product.domain.entity.Product;
import com.microcommerce.product.domain.entity.ProductImage;
import com.microcommerce.product.domain.vo.CreateProductVo;
import com.microcommerce.product.exception.ProductException;
import com.microcommerce.product.exception.ProductExceptionCode;
import com.microcommerce.product.infrastructure.repository.ProductImageRepository;
import com.microcommerce.product.infrastructure.repository.ProductRepository;
import com.microcommerce.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    private final MemberClientDao memberClientDao;

    private final ProductMapper productMapper;

    public CreateProductResDto createProduct(final CreateProductVo data) {
        final ProfileResDto profile = memberClientDao.getProfile(data.sellerId());
        final Product product = productRepository.save(productMapper.toProduct(data, profile.name()));

        final List<ProductImage> images = new ArrayList<>();
        for (int i = 0; i < data.imageUrls().size(); i++) {
            images.add(productMapper.toProductImage(product.getId(), data.imageUrls().get(i), i + 1));
        }
        productImageRepository.saveAll(images);

        return productMapper.toCreateProductResDto(product);
    }

    @Cacheable(value = "GetProducts", key = "#pageRequest", cacheManager = "cacheManager")
    public PageDto<ProductResDto> getProducts(PageRequest pageRequest) {
        final Page<ProductResDto> products = productRepository.findPageBy(pageRequest)
                .map(p -> {
                    final ProductImage representativeImage = productImageRepository.findFirstByProductIdOrderByDisplayOrder(p.getId());
                    return productMapper.toProductResDto(p, representativeImage.getUrl());
                });
        return new PageDto<>(products.getContent(), pageRequest.getPageNumber(), pageRequest.getPageSize(), products.getTotalElements());
    }

    public List<ProductResDto> getProductsByIds(final List<Long> ids) {
        return productRepository.getProducts(ids);
    }

    public ProductDetailResDto getProduct(final Long productId) {
        return productRepository.findById(productId)
                .map(p -> {
                    final List<String> images = productImageRepository.findAllByProductIdOrderByDisplayOrder(productId).stream()
                            .map(ProductImage::getUrl)
                            .toList();
                    return productMapper.toProductDetailResDto(p, images);
                })
                .orElseThrow(() -> new ProductException(ProductExceptionCode.UNAUTHORIZED));
    }

}
