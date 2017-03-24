package com.sluka.taras.service.serviceImpl;

import com.sluka.taras.service.DescriptionService;
import com.sluka.taras.common.model.Description;
import com.sluka.taras.repository.DescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DescriptionServiceImpl implements DescriptionService {

    @Autowired
    DescriptionRepository descriptionRepository;

    public void deleteAllInBatch() {
        descriptionRepository.deleteAllInBatch();
    }

    public final Description save(final Description descriptions) {
        return descriptionRepository.save(descriptions);
    }

    public final void delete(final Description descriptions) {
        descriptionRepository.delete(descriptions);
    }

    public final List<Description> findAll() {
        return descriptionRepository.findAll();
    }

    public final Description findById(final Long id) {
        return descriptionRepository.findOne(id);
    }

    public final long count() {
        return descriptionRepository.count();
    }

    public void deleteById(long id) {
        descriptionRepository.delete(id);
    }

}
