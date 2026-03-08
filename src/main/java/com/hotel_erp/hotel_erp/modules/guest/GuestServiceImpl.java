package com.hotel_erp.hotel_erp.modules.guest;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class GuestServiceImpl extends BaseServiceImpl<GuestEntity, Long, GuestRepository> implements GuestService {
    public GuestServiceImpl(GuestRepository repository) {
        super(repository);
    }
}
