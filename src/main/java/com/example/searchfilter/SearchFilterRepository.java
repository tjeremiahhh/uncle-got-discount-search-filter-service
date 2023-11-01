package com.example.searchfilter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<SearchListingDTO> searchBusinessListings(String outletName, List<Integer> cuisineList, int minDiscount, int maxDiscount, Pageable pageable) {
        MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("outletName", outletName);
        sqlParams.addValue("minDiscount", minDiscount);
        sqlParams.addValue("maxDiscount", maxDiscount);

        String sqlQuery = 
        " SELECT business_listings.id, business_listings.outlet_name, business_listings.address, business_listings.image_file,   "
        + " business_listing_description.cuisines "
        + " FROM business_listings   "
        + " INNER JOIN business_listing_description ON business_listings.id = business_listing_description.business_listing_id   "
        + " LEFT JOIN business_listing_discounts ON business_listings.id = business_listing_discounts.business_listing_id  "
        + " INNER JOIN discounts ON business_listing_discounts.discounts_id = discounts.id "
        + " WHERE LOWER(business_listings.outlet_name) LIKE CONCAT('%', LOWER(:outletName), '%') "
        + " AND discounts.discount >= :minDiscount AND discounts.discount <= :maxDiscount ";

        if (!cuisineList.isEmpty()) {
            sqlQuery += " AND (";

            for (int i = 0; i < cuisineList.size(); i++) {
                sqlParams.addValue("cuisine" + i, cuisineList.get(i));

                if (i == 0) {
                    sqlQuery += " POSITION(CONCAT('|', :cuisine" + i + " ,'|') IN business_listing_description.cuisines) > 0 ";
                } else {
                    sqlQuery += " OR POSITION(CONCAT('|', :cuisine" + i + " ,'|') IN business_listing_description.cuisines) > 0 ";
                }
            }

            sqlQuery += " ) ";
        } else {
            sqlQuery += " AND business_listing_description.cuisines IS NULL ";
        }

        sqlQuery += " GROUP BY outlet_name ";

        return queryPage(sqlQuery, sqlParams, pageable, SearchListingDTO.class);
    }
}
