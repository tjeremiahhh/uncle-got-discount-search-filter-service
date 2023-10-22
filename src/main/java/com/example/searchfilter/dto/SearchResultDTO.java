package com.example.searchfilter.dto;

import lombok.Data;

@Data
public class SearchResultDTO {
    private Integer id;
    private String outletName;
    private String address;
    private byte[] imageFile;
    private String cuisines;
}
