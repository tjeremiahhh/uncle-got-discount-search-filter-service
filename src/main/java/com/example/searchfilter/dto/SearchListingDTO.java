package com.example.searchfilter.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchListingDTO extends SearchResultDTO {
    private List<BusinessListingDiscountDTO> discountList;

    public SearchListingDTO(SearchResultDTO searchResultDTO) {
        this.setId(searchResultDTO.getId());
        this.setOutletName(searchResultDTO.getOutletName());
        this.setAddress(searchResultDTO.getAddress());
        this.setCuisines(searchResultDTO.getCuisines());
        this.setImageFile(searchResultDTO.getImageFile());
    }
}
