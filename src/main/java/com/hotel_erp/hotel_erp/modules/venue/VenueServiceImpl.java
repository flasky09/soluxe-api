package com.hotel_erp.hotel_erp.modules.venue;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VenueServiceImpl extends BaseServiceImpl<VenueEntity, Long, VenueRepository> implements VenueService {
    public VenueServiceImpl(VenueRepository repository) {
        super(repository);
    }
}

