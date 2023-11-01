package com.example.searchfilter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.searchfilter.dto.SearchListingDTO;
import com.example.searchfilter.dto.SearchListingsRequestDTO;
import com.example.searchfilter.dto.SearchResultDTO;
import com.example.searchfilter.entity.Cuisine;
import com.example.searchfilter.entity.Discount;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/search-filter/", produces = "application/json; charset=utf-8")
@CrossOrigin(origins = {"http://localhost:4200", "http://ugd-frontend-app-lb-1592138430.ap-southeast-1.elb.amazonaws.com/"})
@RequiredArgsConstructor
public class SearchFilterController {

    private final SearchFilterService searchFilterService;

    @GetMapping(value = "search-by-outlet-name")
    public List<SearchResultDTO> searchByOutletName(
        @RequestParam String outletName, 
        @RequestParam(required = false) Integer cuisine) {
        return searchFilterService.searchByOutletName(outletName, cuisine);
    }

    @GetMapping(value = "get-all-cuisines")
    public List<Cuisine> getAllCuisines() {
        return searchFilterService.getAllCuisines();
    }

    @GetMapping(value = "get-all-discounts")
    public List<Discount> getAllDiscounts() {
        return searchFilterService.getAllDiscounts();
    }

    @PostMapping(value = "search-listings", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Page<SearchListingDTO> searchListings(
        @RequestBody SearchListingsRequestDTO searchListingsRequestDTO,
        Pageable pageable
        ) {
        return searchFilterService.searchListings(
            searchListingsRequestDTO.getOutletName(), 
            searchListingsRequestDTO.getCuisineList(), 
            searchListingsRequestDTO.getMinDiscount(), 
            searchListingsRequestDTO.getMaxDiscount(),
            pageable
            );
    }
}
