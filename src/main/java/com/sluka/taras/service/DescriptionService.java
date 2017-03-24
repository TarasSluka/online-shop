package com.sluka.taras.service;

import com.sluka.taras.common.model.Description;

import java.util.List;

public interface DescriptionService {

    void deleteAllInBatch();

    Description save( Description descriptions);

    void delete( Description user);

    List<Description> findAll();

    Description findById( Long id);

    void deleteById( long id);

}
