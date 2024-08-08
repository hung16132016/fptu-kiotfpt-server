package com.kiotfpt.response;

import java.time.LocalDateTime;
import java.util.List;

import com.kiotfpt.model.ProductThumbnail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatisResponse {
    private int id;
    private String name;
    private List<ProductThumbnail> thumbnails;
    private int bought_quantity;
    private float total;
    private LocalDateTime timeComplete;
    private VariantResponse variant;
    private ShopMiniResponse shop;
}