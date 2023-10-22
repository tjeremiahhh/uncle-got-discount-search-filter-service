package com.example.searchfilter.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("cuisines")
public class Cuisine {
    @Id
    private int id;
    private String cuisine;
}
