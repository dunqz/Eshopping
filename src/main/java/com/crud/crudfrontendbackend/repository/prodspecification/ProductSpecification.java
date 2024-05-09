package com.crud.crudfrontendbackend.repository.prodspecification;

import com.crud.crudfrontendbackend.model.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public interface ProductSpecification {
    static Specification<Product> filterItemStore(String productName, String[] classify){
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(productName != null && !productName.isEmpty()){
                predicates.add(cb.like(root.get("productName"), "%" + productName + "%"));
            }
            if(classify != null && classify.length > 0){
                CriteriaBuilder.In<String> in = cb.in(root.get("classify"));
                for (String value : classify) {
                    in.value(value);
                }
                predicates.add(in);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}

//    static Specification<Product> hasProductName(String productName) {
//        return (root, query, cb) -> productName == null ? null : cb.like(root.get("productName"), "%" + productName + "%");
//    }
//    static Specification<Product> hasClassify(String classify) {
//        return (root, query, cb) -> classify == null ? null : cb.equal(root.get("classify"), classify);
//    }