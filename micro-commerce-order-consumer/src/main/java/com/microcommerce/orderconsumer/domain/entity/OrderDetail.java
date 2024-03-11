package com.microcommerce.orderconsumer.domain.entity;

import com.microcommerce.orderconsumer.domain.enums.OrderDetailStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productImage;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private String sellerName;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderDetailStatus status;

    public void setProductInfo(String productName, String productImage, Long price, String sellerName) {
        this.productName = productName;
        this.productImage = productImage;
        this.price = price;
        this.sellerName = sellerName;
    }

}
