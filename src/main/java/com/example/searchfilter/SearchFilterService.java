package com.example.searchfilter;

import java.util.Arrays;
import java.util.List;

import javax.naming.directory.SearchResult;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.searchfilter.dto.SearchResultDTO;
import com.example.searchfilter.entity.Cuisine;

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
        return searchFilterRepository.findAll(Cuisine.class);
    }
}
