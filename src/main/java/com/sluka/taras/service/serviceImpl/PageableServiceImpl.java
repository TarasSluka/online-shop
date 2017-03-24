package com.sluka.taras.service.serviceImpl;

import com.sluka.taras.service.PageableService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PageableServiceImpl implements PageableService {

    @Override
    public Pageable getPageable(Integer page, Integer size, String sortType, String sortParam) {
        Sort sort = null;
        if (!StringUtils.isEmpty(sortParam) && !StringUtils.isEmpty(sortType)) {
            if (sortType.equals("false"))
                sort = new Sort(Sort.Direction.DESC, sortParam);
            else
                sort = new Sort(Sort.Direction.ASC, sortParam);
        } else
            sort = new Sort(Sort.Direction.DESC, "createdDate");
        Pageable pageable = new PageRequest(
                ((page == null) || (page == 0) ? 0 : (page - 1)),
                ((size == null) || (size == 0) ? 20 : size),
                sort);
        return pageable;
    }

}
