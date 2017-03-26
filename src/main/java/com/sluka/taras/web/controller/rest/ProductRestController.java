package com.sluka.taras.web.controller.rest;

import com.sluka.taras.service.PageableService;
import com.sluka.taras.service.ProductService;
import com.sluka.taras.common.dto.ProductDto;
import com.sluka.taras.common.mapper.ProductMapper;
import com.sluka.taras.web.model.ProductFilterRequest;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("api/product")
public class ProductRestController {

    private final Logger logger = LogManager.getLogger(ProductRestController.class);

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final PageableService pageableService;

    @Autowired
    ProductRestController(ProductService productService, ProductMapper productMapper, PageableService pageableService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.pageableService = pageableService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<ProductDto>> getProduct(ProductFilterRequest productFilterRequest) {
        Pageable pageable = pageableService.getPageable(
                productFilterRequest.getPage(),
                productFilterRequest.getSize(),
                productFilterRequest.getSortType(),
                productFilterRequest.getSortParam());
        Page<ProductDto> productDtoList = productService.filter(productFilterRequest, pageable);
        logger.info("......" + productFilterRequest);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    @RequestMapping(value = "/min&MaxPrice", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Long>> getMaxAndMinPrice(ProductFilterRequest productFilterRequest) {
        Map<String, Long> map = productService.getMaxAndMinPrice(productFilterRequest);
        return new ResponseEntity<>(map,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long id) {
        logger.info("getProduct id = " + id);
        ProductDto product = productMapper.toDto(productService.findById(id));
        if (product == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<ProductDto>(product, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Long> createCategory(@RequestBody ProductDto productDto) {
        logger.info(productDto);
        productDto = productService.save(productDto);
        if (productDto == null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        return new ResponseEntity<>(productDto.getId(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") long id, @RequestBody ProductDto productDto) {
        if (productService.findById((productDto.getId())) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        productDto = productService.update(productDto);
        if (productDto != null)
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") long id) {
        if (productService.findById(id) == null)
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
