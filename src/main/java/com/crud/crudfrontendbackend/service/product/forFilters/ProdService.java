package com.crud.crudfrontendbackend.service.product.forFilters;

import com.crud.crudfrontendbackend.dto.ProductFilter;
import com.crud.crudfrontendbackend.repository.prodspecification.ProductRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

//import static com.crud.crudfrontendbackend.repository.prodspecification.ProductSpecification.hasClassify;
//import static com.crud.crudfrontendbackend.repository.prodspecification.ProductSpecification.hasProductName;

@Service
public class ProdService {

    private final ProductRepositoryImpl productRepository;

    public ProdService(ProductRepositoryImpl productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductFilter> filterItemStore(String productName, String[] classify,String order) {
        List<ProductFilter> filteredProducts =  productRepository.filterItemStore(productName, classify, order);

        List<ProductFilter> productFilters = new ArrayList<>();
        for ( ProductFilter product : filteredProducts) {
            ProductFilter productDto = convertToDtoWithBase64Image(product);
            productFilters.add(productDto);
        }

        return productFilters;
    }

    private ProductFilter convertToDtoWithBase64Image(ProductFilter product) {
        ProductFilter productFilter = new ProductFilter();
        BeanUtils.copyProperties(product, productFilter,"image");

        try {
            // Read the image file from the file system
            byte[] imageData = Files.readAllBytes(Paths.get(product.getImage()));

            // Convert image data to Base64
            String base64Image = Base64.getEncoder().encodeToString(imageData);
            productFilter.setImage(base64Image);
        } catch (IOException e) {
            // Handle the exception if the image file cannot be read
            e.printStackTrace();
        }

        return productFilter;
    }

}
