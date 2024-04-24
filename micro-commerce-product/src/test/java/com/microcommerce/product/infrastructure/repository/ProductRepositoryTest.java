package com.microcommerce.product.infrastructure.repository;

import com.microcommerce.product.config.TestQueryDslConfig;
import com.microcommerce.product.domain.entity.Product;
import com.microcommerce.product.domain.enums.ProductStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.yml")
@Import(TestQueryDslConfig.class)
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    static final int DATA_NUMBER = 30;

    static List<Product> newProducts;

    @BeforeAll
    static void beforeAll() {
        newProducts = LongStream.range(1, DATA_NUMBER + 1)
                .mapToObj(i -> new Product(i, Math.abs(i - (DATA_NUMBER + 1)), "seller name", "product name", 1000,
                        "category", "description", ProductStatus.AVAILABLE, 100))
                .toList();
    }

    @BeforeEach
    void beforeEach() {
        newProducts.stream()
                .filter(p -> !productRepository.existsById(p.getId()))
                .forEach(p -> productRepository.save(p));
    }

    @DisplayName("페이지네이션 잘 동작하는지 테스트")
    @Test
    void findSlice() {
        final int size = 10;

        final PageRequest pageRequest = PageRequest.of(0, size);
        final Slice<Product> products = productRepository.findPageBy(pageRequest);
        assertThat(products.getNumberOfElements()).isEqualTo(size);
        final List<Product> productList = products.toList();

        productRepository.findAll().forEach(System.out::println);

        IntStream.range(0, size)
                .forEach(i -> assertThat(productList.get(i).getSellerId())
                        .isEqualTo(Math.abs(i - DATA_NUMBER)));
    }

    @DisplayName("페이지네이션 잘 동작하는지 테스트 (정렬 포함)")
    @Test
    void findSliceSort() {
        final int size = 10;

        final PageRequest pageRequest = PageRequest.of(0, size, Sort.by("sellerId"));
        final Slice<Product> products = productRepository.findPageBy(pageRequest);
        assertThat(products.getNumberOfElements()).isEqualTo(size);

        final List<Product> productList = products.toList();
        IntStream.range(0, size)
                .forEach(i -> assertThat(productList.get(i).getId()).isEqualTo(DATA_NUMBER - i));
        productRepository.findAll().forEach(System.out::println);
    }

}