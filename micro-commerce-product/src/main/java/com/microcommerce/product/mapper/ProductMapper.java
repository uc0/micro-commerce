package com.microcommerce.product.mapper;

import com.microcommerce.product.domain.dto.req.CreateProductReqDto;
import com.microcommerce.product.domain.dto.res.CreateProductResDto;
import com.microcommerce.product.domain.dto.res.ProductDetailResDto;
import com.microcommerce.product.domain.dto.res.ProductResDto;
import com.microcommerce.product.domain.entity.Product;
import com.microcommerce.product.domain.entity.ProductImage;
import com.microcommerce.product.domain.vo.CreateProductVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "name", target = "productName")
    CreateProductResDto toCreateProductResDto(Product product);

    CreateProductVo toCreateProductVo(CreateProductReqDto req);

    @Mapping(source = "images", target = "imageUrl")
    ProductDetailResDto toProductDetailResDto(Product product, List<String> images);

    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "representativeImageUrl", source = "representativeImageUrl")
    @Mapping(target = "sellerId", source = "product.sellerId")
    @Mapping(target = "sellerName", source = "product.sellerName")
    @Mapping(target = "stock", source = "product.stock")
    ProductResDto toProductResDto(Product product, String representativeImageUrl);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Product toProduct(CreateProductVo data, String sellerName);

    @Mapping(target = "id", ignore = true)
    ProductImage toProductImage(Long productId, String url, int displayOrder);

}
