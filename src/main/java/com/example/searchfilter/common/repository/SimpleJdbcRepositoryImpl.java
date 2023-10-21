package com.example.searchfilter.common.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import com.example.searchfilter.common.utility.SqlUtil;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class SimpleJdbcRepositoryImpl implements SimpleJdbcRepository {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    protected JdbcAggregateTemplate jdbcAggregateTemplate;

    @Override
    public <T> T save(T entity) {
        return jdbcAggregateTemplate.save(entity);
    }

    @Override
    public <T> void deleteById(Object id, Class<T> entityType) {
        jdbcAggregateTemplate.deleteById(id, entityType);
    }

    @Override
    public <T> void delete(T entity) {
        jdbcAggregateTemplate.delete(entity);
    }

    @Override
    public <T> List<T> findAll(Class<T> entityType) {
        List<T> resultList = new ArrayList<>();
        jdbcAggregateTemplate.findAll(entityType).forEach(resultList::add);
        return resultList;
    }

    @Override
    public <T> T findById(Object id, Class<T> entityType) {
        return jdbcAggregateTemplate.findById(id, entityType);
    }

    @Override
    public int update(String sql, MapSqlParameterSource params) {
        int result = namedParameterJdbcTemplate.update(sql, params);
        if (log.isDebugEnabled()) {
            log.debug("{} row(s) affected", result);
        }
        return result;
    }

    @Override
    public <T> T querySingleObject(String sql, Class<T> clazz) {
        return querySingleObject(sql, new MapSqlParameterSource(), clazz);
    }

    @Override
    public <T> T querySingleObject(String sql, MapSqlParameterSource params, Class<T> clazz) {
        T result;
        try {
            if (isGeneralClass(clazz)) {
                result = namedParameterJdbcTemplate.queryForObject(sql, params, clazz);

            } else {
                result = namedParameterJdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<T>(clazz));
            }
        } catch (EmptyResultDataAccessException e) { // if no result return, EmptyResultDataAccessException will be thrown
            result = null;
        }

        if (log.isDebugEnabled()) {
            log.debug("Query returned {} result", result == null ? 0 : 1);
        }
        return result;
    }

    @Override
    public <T> List<T> queryList(String sql, Class<T> clazz) {
        return this.queryList(sql, new MapSqlParameterSource(), clazz);
    }

    @Override
    public <T> List<T> queryList(String sql, MapSqlParameterSource params, Class<T> clazz) {
        List<T> result;
        if (isGeneralClass(clazz)) {
            result = namedParameterJdbcTemplate.queryForList(sql, params, clazz);
        } else {
            result = namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<T>(clazz));
        }
        if (log.isDebugEnabled()) {
            log.debug("Query returned {} result(s)", CollectionUtils.isEmpty(result) ? 0 : result.size());
        }
        return result;
    }

    @Override
    public <T> Page<T> queryPage(String sql, MapSqlParameterSource params, Pageable pageable, Class<T> clazz) {
        String countQuery = SqlUtil.deriveCountQuery(sql);
        return queryPage(sql, countQuery, params, pageable, clazz);
    }

    @Override
    public <T> Page<T> queryPage(String sql, MapSqlParameterSource params, Pageable pageable, String defaultOrder, Class<T> clazz) {
        String countQuery = SqlUtil.deriveCountQuery(sql);
        return queryPage(sql, countQuery, params, pageable, defaultOrder, clazz);
    }

    @Override
    public <T> Page<T> queryPage(String sql, String countQuery, MapSqlParameterSource params, Pageable pageable, Class<T> clazz) {

        String defaultOrder = " ORDER BY (SELECT NULL) ";
        return queryPage(sql, countQuery, params, pageable, defaultOrder, clazz);
    }

    @Override
    public <T> Page<T> queryPage(String sql, String countQuery, MapSqlParameterSource params, Pageable pageable, String defaultOrder, Class<T> clazz) {
        Long count = querySingleObject(countQuery, params, Long.class);
        List<T> result;

        if (isGeneralClass(clazz)) {
            result = namedParameterJdbcTemplate.queryForList(sql + SqlUtil.sortBuilder(pageable, defaultOrder), params, clazz);
        } else {
            result = namedParameterJdbcTemplate.query(sql + SqlUtil.sortBuilder(pageable, defaultOrder), params, new BeanPropertyRowMapper<T>(clazz));
        }

        if (log.isDebugEnabled()) {
            log.debug("Query returned {} result(s)", CollectionUtils.isEmpty(result) ? 0 : result.size());
        }
        return new PageImpl<>(result, pageable, count);
    }

    private boolean isGeneralClass(Class<?> clazz) {
        return !clazz.getPackage().getName().startsWith("com");
    }
}
