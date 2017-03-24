package com.sluka.taras.web.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PaginationRequest {
    String sort;
    Integer page;
    Integer size;
}
