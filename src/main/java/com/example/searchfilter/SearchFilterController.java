package com.example.searchfilter;

import java.util.List;

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

import com.example.searchfilter.dto.SearchResultDTO;
import com.example.searchfilter.entity.Cuisine;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search-filter/")
@CrossOrigin(origins = {"http://localhost:4200", "http://ugd-frontend-app-lb-1592138430.ap-southeast-1.elb.amazonaws.com/"})
@RequiredArgsConstructor
public class SearchFilterController {

    private final SearchFilterService searchFilterService;

    @GetMapping(value = "search-by-outlet-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SearchResultDTO> searchByOutletName(
        @RequestParam String outletName, 
        @RequestParam(required = false) Integer cuisine) {
        return searchFilterService.searchByOutletName(outletName, cuisine);
    }

    @GetMapping(value = "get-all-cuisines", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Cuisine> getAllCuisines() {
        return searchFilterService.getAllCuisines();
    }
}
