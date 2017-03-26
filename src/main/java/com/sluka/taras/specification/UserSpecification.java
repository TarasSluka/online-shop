package com.sluka.taras.specification;

import com.sluka.taras.common.model.User;
import com.sluka.taras.web.model.UserFilterRequest;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserSpecification implements Specification<User> {
    private final Logger logger = LogManager.getLogger(UserSpecification.class);
    private UserFilterRequest filterRequest;

    public void setFilter(UserFilterRequest filterRequest) {
        this.filterRequest = filterRequest;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        assert filterRequest != null;
        Predicate predicate = null;
        String globalValue = filterRequest.getGlobal();
        List<Predicate> predicateList = new ArrayList<>();
        if (filterRequest.getId() != null) {
            predicateList.add(cb.like(root.get("id").as(String.class), toLike(filterRequest.getId().toString())));
        }
        if (!StringUtils.isEmpty(filterRequest.getFirstName())) {
            predicateList.add(cb.like(root.get("firstName"), toLike(filterRequest.getFirstName())));
        }
        if (!StringUtils.isEmpty(filterRequest.getLastName())) {
            predicateList.add(cb.like(root.get("lastName"), toLike(filterRequest.getLastName())));
        }
        if (!StringUtils.isEmpty(filterRequest.getEmail())) {
            predicateList.add(cb.like(root.get("email"), toLike(filterRequest.getEmail())));
        }
        if (!StringUtils.isEmpty(filterRequest.getUserName())) {
            predicateList.add(cb.like(root.get("userName"), toLike(filterRequest.getUserName())));
        }
        if (!StringUtils.isEmpty(filterRequest.getCreatedDate())) {
            predicateList.add(cb.between(root.get("registerDate").as(Date.class), filterRequest.getCreatedDate(), getNextDay(filterRequest.getCreatedDate())));
        }
        if (!StringUtils.isEmpty(filterRequest.getSex()) && !filterRequest.getSex().equals("All")) {
            predicateList.add(cb.equal(root.get("sex").as(String.class), filterRequest.getSex()));
        }
        if (!StringUtils.isEmpty(filterRequest.getBan()) && !filterRequest.getBan().equals("All")) {
            predicateList.add(cb.equal(root.get("enabled"), Boolean.valueOf(filterRequest.getBan())));
        }
        if (!StringUtils.isEmpty(globalValue)) {
            String likeGlobalValue = toLike(globalValue);
            predicateList.add(cb.or(
                    cb.like(root.get("firstName"), likeGlobalValue)
                    , cb.like(root.get("lastName"), likeGlobalValue)
                    , cb.like(root.get("email"), likeGlobalValue)
                    , cb.like(root.get("userName"), likeGlobalValue)
                    , cb.like(root.get("id").as(String.class), likeGlobalValue)));
        }
        predicate = cb.and(predicateList.toArray(new Predicate[]{}));
        return predicate;
    }

    private Date getNextDay(Date date) {
        return new Date(date.getTime() + 86400000);
    }

    private String toLike(String s) {
        return s + "%";
    }
}