package com.sluka.taras.service.serviceImpl;

import com.sluka.taras.service.*;
import com.sluka.taras.common.dto.DescriptionDto;
import com.sluka.taras.common.dto.ProductDto;
import com.sluka.taras.common.mapper.ProductMapper;
import com.sluka.taras.common.model.Cart;
import com.sluka.taras.common.model.Category;
import com.sluka.taras.common.model.Description;
import com.sluka.taras.common.model.Product;
import com.sluka.taras.repository.ProductRepository;
import com.sluka.taras.specification.ProductMaxPriceSpecification;
import com.sluka.taras.specification.ProductSpecification;
import com.sluka.taras.web.model.ProductFilterRequest;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    private PageableService pageableService;
    private ProductRepository productRepository;
    private ProductSpecification productSpecification;
    private ProductMaxPriceSpecification productMaxPriceSpecification;
    private ProductMapper productMapper;
    private DescriptionService descriptionService;
    private CartService cartService;
    private CategoryService categoryService;
    @PersistenceContext
    private EntityManager em;

    @Autowired
    ProductServiceImpl(ProductMaxPriceSpecification productMaxPriceSpecification, ProductMapper productMapper, ProductSpecification productSpecification, ProductRepository productRepository, PageableService pageableService, DescriptionService descriptionService, CartService cartService, CategoryService categoryService) {
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.productMapper = productMapper;
        this.productSpecification = productSpecification;
        this.productRepository = productRepository;
        this.pageableService = pageableService;
        this.productMaxPriceSpecification = productMaxPriceSpecification;
        this.descriptionService = descriptionService;
    }

    @Override

    public ProductDto update(ProductDto productDto) {
        Product newProduct = productMapper.toEntity(productDto);
        if (newProduct.getId() == null || newProduct.getPrice() == null)
            return null;
        List<Description> oldDescriptionList = findById(productDto.getId()).getDescriptionsArrayList();
        List<Description> newDescriptionList = newProduct.getDescriptionsArrayList();

        List<Long> longArrayList = new ArrayList<>();
        oldDescriptionList.forEach(description -> longArrayList.add(description.getId()));
        for (int i = 0; i < newDescriptionList.size(); i++) {
            if (newDescriptionList.get(i).getId() != null) {
                Integer tmp = contains(longArrayList, newDescriptionList.get(i).getId());
                if (tmp != null)
                    longArrayList.remove(tmp.intValue());
            }
        }
        Product product = productRepository.save(newProduct);
        if (longArrayList.size() > 0) {
            longArrayList.forEach(item -> {
                descriptionService.deleteById(item);
            });
        }
        if (newProduct == null)
            return null;
        return productMapper.toDto(newProduct);
    }

    public List<Product> findByCategory(Category category) {
        List<Product> productList = null;
        if (category == null)
            return null;
        else
            productList = productRepository.findByCategory(category);
        return productList;
    }

    public void deleteCategoryFromProduct(Category category) {
        if (category != null) {
            List<Product> productList = findByCategory(category);
            productList.forEach(item -> {
                item.setCategory(null);
                productRepository.save(item);
            });
        }
    }

    public void deleteById(Long id) {
        Product product = productRepository.findOne(id);
        List<Description> oldDescriptionList = findById(id).getDescriptionsArrayList();
        List<Cart> cartList = cartService.getAllCartByProductId(id);
        if (cartList.size() > 0) {
            cartList.forEach(item -> {
                cartService.deleteById(item.getId());
            });
        }
        product.setCategory(null);
        product.getDescriptionsArrayList().removeAll(product.getDescriptionsArrayList());
        oldDescriptionList.forEach(item -> {
            descriptionService.deleteById(item.getId());
        });
        productRepository.save(product);
        productRepository.delete(id);
    }

    public Integer contains(List<Long> longArrayList, Long id) {
        Integer f = null;
        for (int i = 0; i < longArrayList.size(); i++) {
            if (longArrayList.get(i) == id) {
                f = i;
                break;
            }
        }
        return f;
    }

    public Long checkExistDescription(Description description, List<Description> descriptionList) {
        Long id = null;
        for (Description description1 : descriptionList)
            if (description.getId() == description.getId())
                id = description.getId();
        return id;
    }


    public ProductDto save(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        List<DescriptionDto> descriptionDtoList = productDto.getDescriptionList();
        List<Description> descriptionList = new ArrayList<>();

        for (int i = 0; i < descriptionDtoList.size(); i++) {
            Description description = new Description();
            description.setType(descriptionDtoList.get(i).getType());
            description.setValue(descriptionDtoList.get(i).getValue());
            descriptionService.save(description);
            descriptionList.add(description);
        }
        product.getDescriptionsArrayList().addAll(descriptionList);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }


    public final Product save(final Product product) {
        productRepository.save(product);
        return product;
    }


    public final Product update(final Product product) {
        return productRepository.save(product);
    }


    public final void delete(final Product product) {
        productRepository.findOne(product.getId()).setCategory(null);
        productRepository.delete(product);

    }

    public final List<Product> findAll() {
        return productRepository.findAll();
    }

    public final long count() {
        return productRepository.count();
    }

    public boolean exists(Long id) {
        return productRepository.exists(id);
    }


    @Override
    public Page<ProductDto> filter(ProductFilterRequest productFilterRequest, Pageable pageable) {
        productSpecification.setFilter(productFilterRequest);
        Page<Product> productPage = productRepository.findAll(productSpecification, pageable);
        return productMapper.toDto(pageable, productPage);
    }

    List<ProductDto> toDto(List<Product> productList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        productList.forEach(item -> {
            productDtoList.add(productMapper.toDto(item));
        });
        return productDtoList;
    }

    public Map<String, Long> getMaxAndMinPrice(ProductFilterRequest productFilterRequest) {
        productSpecification.setFilter(productFilterRequest);
        Sort sort = new Sort(Sort.Direction.ASC, "price");
        List<Product> productPageMin = productRepository.findAll(productSpecification, sort);
        sort = new Sort(Sort.Direction.DESC, "price");
        List<Product> productPageMax = productRepository.findAll(productSpecification, sort);
        Map<String, Long> map = new HashMap<>();
        if (productPageMin.size() != 0 && productPageMin != null && productPageMax.size() != 0 && productPageMax != null) {
            map.put("minPrice", productPageMin.get(0).getPrice());
            map.put("maxPrice", productPageMax.get(0).getPrice());
        }
        return map;
    }

    public final Product findById(final Long id) {
        return productRepository.findOne(id);
    }

}
