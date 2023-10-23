package com.example.searchfilter.dto;

import java.util.List;

import lombok.Data;

@Data
public class SearchListingsRequestDTO {
    private String outletName;
    private List<Integer> cuisineList;
    private int minDiscount;
    private int maxDiscount;
}
