package com.microcommerce.product.domain.dto.res;

import java.util.List;

public record PageDto<T> (List<T> content,
                          int page,
                          int size,
                          long total) {
}
