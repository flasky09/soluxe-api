package com.hotel_erp.hotel_erp.shared;

import java.util.List;
import java.util.Optional;

public interface BaseService<ENTITY extends BaseEntity, ID_TYPE> {

    List<ENTITY> findAll();

    ENTITY save(ENTITY entity);

    Optional<ENTITY> findById(ID_TYPE identifier);

    void deleteById(ID_TYPE identifier);

}
