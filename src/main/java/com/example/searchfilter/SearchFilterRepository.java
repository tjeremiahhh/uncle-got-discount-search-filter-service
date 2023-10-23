package com.example.searchfilter;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.searchfilter.common.repository.SimpleJdbcRepositoryImpl;
import com.example.searchfilter.dto.BusinessListingDiscountDTO;
import com.example.searchfilter.dto.SearchListingDTO;
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

    public List<BusinessListingDiscountDTO> searchBusinessListingDiscount(int businessListingId, int minDiscount, int maxDiscount) {
        String sqlQuery =
        " SELECT   "
        + " 	business_listing_discounts.day_id, days.day,  "
        + " 	business_listing_discounts.timings_id, timings.time, "
        + " 	business_listing_discounts.discounts_id, discounts.discount "
        + " FROM business_listing_discounts "
        + " LEFT JOIN days ON days.id = business_listing_discounts.day_id "
        + " LEFT JOIN timings ON timings.id = business_listing_discounts.timings_id "
        + " LEFT JOIN discounts ON discounts.id = business_listing_discounts.discounts_id "
        + " WHERE business_listing_discounts.business_listing_id = :businessListingId "
        + " 	AND discounts.discount >= :minDiscount AND discounts.discount <= :maxDiscount "
        + " ORDER BY day_id asc, timings_id asc ";

        MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("businessListingId", businessListingId);
        sqlParams.addValue("minDiscount", minDiscount);
        sqlParams.addValue("maxDiscount", maxDiscount);

        return queryList(sqlQuery, sqlParams, BusinessListingDiscountDTO.class);
    }
}
