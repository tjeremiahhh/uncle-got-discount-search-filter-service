package com.example.searchfilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.directory.SearchResult;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.searchfilter.dto.BusinessListingDiscountDTO;
import com.example.searchfilter.dto.SearchListingDTO;
import com.example.searchfilter.dto.SearchResultDTO;
import com.example.searchfilter.entity.Cuisine;
import com.example.searchfilter.entity.Discount;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchFilterService {
    
    private final SearchFilterRepository searchFilterRepository;

    public List<SearchResultDTO> searchByOutletName(String outletName, Integer cuisine) {

        List<SearchResultDTO> searchResultDTOList = searchFilterRepository.searchByOutletName(outletName);

        if (cuisine != null) {
            searchResultDTOList.removeIf((searchResultDTO) -> {
                return !Arrays.asList(searchResultDTO.getCuisines().split("|")).contains(cuisine.toString());
            });
        }

        return searchResultDTOList;
    }

    public List<Cuisine> getAllCuisines() {
        List<Cuisine> cuisineList = searchFilterRepository.findAll(Cuisine.class);

        cuisineList.sort((cuisineA, cuisineB) -> {
            return cuisineA.getCuisine().compareTo(cuisineB.getCuisine());
        });

        return cuisineList;
    }

    public List<Discount> getAllDiscounts() {
        return searchFilterRepository.findAll(Discount.class);
    }

    public List<SearchListingDTO> searchListings(String outletName, List<Integer> cuisineList, int minDiscount, int maxDiscount) {
        List<SearchResultDTO> searchResultDTOList = searchFilterRepository.searchByOutletName(outletName);

        searchResultDTOList.removeIf((searchResult) -> {
            List<String> searchResultCuisineList = new ArrayList<String>(Arrays.asList(searchResult.getCuisines().split("|")));
            List<String> cuisineStringList = cuisineList.stream().map(cuisine -> cuisine.toString()).collect(Collectors.toList());
            return !searchResultCuisineList.removeAll(cuisineStringList);
        });

        List<SearchListingDTO> searchListingDTOList = new ArrayList<>();

        searchResultDTOList.forEach((searchResult) -> {
            List<BusinessListingDiscountDTO> businessListingDiscountDTOList = 
                searchFilterRepository.searchBusinessListingDiscount(searchResult.getId(), minDiscount, maxDiscount);

            if (businessListingDiscountDTOList.size() > 0) {
                SearchListingDTO searchListingDTO = new SearchListingDTO(searchResult);
                searchListingDTO.setDiscountList(businessListingDiscountDTOList);
                searchListingDTOList.add(searchListingDTO);
            }
        });

        return searchListingDTOList;
    }
}
