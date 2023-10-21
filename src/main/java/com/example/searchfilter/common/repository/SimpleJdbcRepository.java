package com.example.searchfilter.common.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public interface SimpleJdbcRepository {

    public <T> T save(T entity);

    public <T> void deleteById(Object id, Class<T> entityType);

    public <T> void delete(T aggregateRoot);

    public <T> T findById(Object id, Class<T> entityType);

    public <T> List<T> findAll(Class<T> entityType);

    public int update(String sql, MapSqlParameterSource params);

    public <T> T querySingleObject(String sql, Class<T> clazz);

    public <T> T querySingleObject(String sql, MapSqlParameterSource params, Class<T> clazz);

    public <T> List<T> queryList(String sql, Class<T> clazz);

    public <T> List<T> queryList(String sql, MapSqlParameterSource params, Class<T> clazz);

    public <T> Page<T> queryPage(String sql, MapSqlParameterSource params, Pageable pageable, Class<T> clazz);

    public <T> Page<T> queryPage(String sql, MapSqlParameterSource params, Pageable pageable, String defaultOrder, Class<T> clazz);

    public <T> Page<T> queryPage(String sql, String countQuery, MapSqlParameterSource params, Pageable pageable, Class<T> clazz);

    public <T> Page<T> queryPage(String sql, String countQuery, MapSqlParameterSource params, Pageable pageable, String defaultOrder, Class<T> clazz);

}
