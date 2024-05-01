package com.microcommerce.product.application;

import com.microcommerce.product.domain.dao.MemberClientDao;
import com.microcommerce.product.infrastructure.repository.ProductImageRepository;
import com.microcommerce.product.infrastructure.repository.ProductRepository;
import com.microcommerce.product.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductImageRepository productImageRepository;

    @Mock
    MemberClientDao memberClientDao;

    @Spy
    ProductMapper productMapper;

    @BeforeEach


    @Test
    void getProducts() {
    }
}