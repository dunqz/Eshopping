package com.crud.crudfrontendbackend.repository;

import com.crud.crudfrontendbackend.model.Product;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class ProdCriteriaBuilder {

    EntityManager em;

    public List<Product> filterItemInStore(String productName, String classify, Integer minPrice, Integer maxPrice, String sort) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);

        Root<Product> productRoot = cq.from(Product.class);

//        Predicate productNamePredicate = cb.equal(productRoot.get("productName"), productName);
//        Predicate classifyPredicate = cb.equal(productRoot.get("classify"), classify);
//        Predicate pricePredicate = cb.lessThanOrEqualTo(productRoot.get("price"), price);
//
//        cq.where(productNamePredicate, classifyPredicate, pricePredicate);

//        In this modified method:
//
//        I added parameters for minPrice, maxPrice, and sort.
//                I constructed predicates based on the given parameters.
//                I added sorting based on the sort parameter.
//                The price range is filtered based on the given minPrice and maxPrice.
//                The result can be sorted in ascending or descending order based on the sort parameter

//        The List<Predicate> predicates is used to collect all the conditions that will be used to filter
//        the results. Each condition added to the predicates list will be combined to form the WHERE clause of the SQL query.

        List<Predicate> predicates = new ArrayList<>();

        // Adding predicates for product name and classification
        if (productName != null) {
            predicates.add(cb.equal(productRoot.get("productName"), "%" +  productName + "%"));
        }
        if (classify != null) {
            predicates.add(cb.equal(productRoot.get("classify"), classify));
        }

        // Adding predicate for price range
        if (minPrice != null && maxPrice != null) {
            predicates.add(cb.between(productRoot.get("price"), minPrice, maxPrice));
        } else if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(productRoot.get("price"), minPrice));
        } else if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(productRoot.get("price"), maxPrice));
        }

        // Constructing the WHERE clause
        cq.where(predicates.toArray(new Predicate[0]));

        // Sorting
        if (sort != null && !sort.isEmpty()) {
            if (sort.equalsIgnoreCase("asc")) {
                cq.orderBy(cb.asc(productRoot.get("price")));
            } else if (sort.equalsIgnoreCase("desc")) {
                cq.orderBy(cb.desc(productRoot.get("price")));
            }
        }

        TypedQuery<Product> query = em.createQuery(cq);
        return query.getResultList();
    }
}
