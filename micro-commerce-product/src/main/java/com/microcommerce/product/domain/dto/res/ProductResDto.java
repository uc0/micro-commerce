package com.microcommerce.product.domain.dto.res;

public record ProductResDto(Long id,
                            String name,
                            Integer price,
                            String representativeImageUrl,
                            Long sellerId,
                            String sellerName,
                            Integer stock) {

}
