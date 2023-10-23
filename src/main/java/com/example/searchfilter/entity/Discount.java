package com.example.searchfilter.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("discounts")
public class Discount {
    @Id
    private int id;
    private int discount;
}
