package com.example.searchfilter;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.searchfilter.common.repository.SimpleJdbcRepositoryImpl;
import com.example.searchfilter.dto.SearchResultDTO;

@Repository
public class SearchFilterRepository extends SimpleJdbcRepositoryImpl {

    public List<SearchResultDTO> searchByOutletName(String outletName) {
        String sqlQuery = 
        "SELECT business_listings.id, business_listings.outlet_name, business_listings.address, business_listings.image_file, " +
        "business_listing_description.cuisines " +
        "FROM business_listings " +
        "INNER JOIN business_listing_description ON business_listings.id = business_listing_description.business_listing_id " +
        "WHERE LOWER(business_listings.outlet_name) LIKE CONCAT('%', LOWER(:outletName), '%')";

        return queryList(sqlQuery, new MapSqlParameterSource("outletName", outletName), SearchResultDTO.class);
    }
}
