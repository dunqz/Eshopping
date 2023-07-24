package com.crud.crudfrontendbackend.service.product;

import com.crud.crudfrontendbackend.dto.ProductDto;
import com.crud.crudfrontendbackend.dto.ProductUpdateDto;
import com.crud.crudfrontendbackend.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product uploadImageProduct(MultipartFile file, String productName);

    Product createProduct(ProductDto productDto);

    Product addProduct(MultipartFile file,ProductDto productDto);

    Product updateProduct(String productName, ProductUpdateDto productUpdateDto);
    List<ProductDto> getAllProduct();
}
