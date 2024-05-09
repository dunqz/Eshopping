package com.crud.crudfrontendbackend.repository.prodspecification;

import com.crud.crudfrontendbackend.dto.ProductDto;
import com.crud.crudfrontendbackend.dto.ProductFilter;
import com.crud.crudfrontendbackend.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@AllArgsConstructor
public class ProductRepositoryImpl {
    private final EntityManager em;

    public List<ProductFilter> filterItemStore(String productName, String[] classify, String order) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductFilter> cq = cb.createQuery(ProductFilter.class);
        Root<Product> root = cq.from(Product.class);


        // Selecting the required fields for ProductFilter
        cq.select(cb.construct(ProductFilter.class,
                root.get("id"),
                root.get("productName"),
                root.get("seller"),
                root.get("isActive"),
                root.get("stock"),
                root.get("price"),
                root.get("image"),
                root.get("classify")));

        Specification<Product> specification = ProductSpecification.filterItemStore(productName, classify, order);
        Predicate predicate = specification.toPredicate(root, cq, cb);
        cq.where(predicate);

        TypedQuery<ProductFilter> query = em.createQuery(cq);
        return query.getResultList();
    }
}
