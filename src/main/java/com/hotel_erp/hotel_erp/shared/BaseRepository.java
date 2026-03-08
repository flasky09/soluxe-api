package com.hotel_erp.hotel_erp.shared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseRepository<ENTITY extends BaseEntity, ID_TYPE> extends JpaRepository<ENTITY, ID_TYPE> {
}
