package com.example.searchfilter.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusinessListingDiscountDTO {
    private int dayId;
    private String day;
    private int timingsId;
    private String time;
    private int discountId;
    private int discount;
}
