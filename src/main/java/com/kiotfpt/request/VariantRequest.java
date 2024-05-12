package com.kiotfpt.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantRequest {

    private int price;
    private int quantity;
    private int colorId;
    private int sizeId;
    private int productId;
}
