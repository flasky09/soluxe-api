package com.hotel_erp.hotel_erp.shared;

import com.hotel_erp.hotel_erp.modules.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<ENTITY extends BaseEntity, ID_TYPE,
        REPOSITORY extends BaseRepository<ENTITY, ID_TYPE>>
        implements BaseService<ENTITY, ID_TYPE> {

    protected final REPOSITORY repository;
    
    @Autowired
    private UserRepository userRepository;

    protected BaseServiceImpl(REPOSITORY repository) {
        this.repository = repository;
    }

    @Override
    public List<ENTITY> findAll() {
        return repository.findAll(
            Sort.by(
                Sort.Direction.DESC, "createdAt"
            )
        );
    }

    @Override
    public ENTITY save(ENTITY entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<ENTITY> findById(ID_TYPE identifier) {
        return repository.findById(identifier);
    }

    @Override
    public void deleteById(ID_TYPE identifier) {
        repository.deleteById(identifier);
    }

    protected void validateUser(Long userId) {
        if (userId == null || !userRepository.existsById(userId)) {
            throw new RuntimeException("Action denied: valid User ID required for audit trail.");
        }
    }
}
