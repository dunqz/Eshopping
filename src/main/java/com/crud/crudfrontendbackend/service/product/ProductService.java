package com.crud.crudfrontendbackend.service.product;

import com.crud.crudfrontendbackend.dto.BuyProductDto;
import com.crud.crudfrontendbackend.dto.CreateProductDto;
import com.crud.crudfrontendbackend.dto.ProductDto;
import com.crud.crudfrontendbackend.dto.ProductUpdateDto;
import com.crud.crudfrontendbackend.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Product uploadImageProduct(MultipartFile file, String productName);

    Product createProduct(CreateProductDto createProductDto);

    //Product addProduct(MultipartFile file,ProductDto productDto);

    Product updateProduct(String productName, ProductUpdateDto productUpdateDto);
    List<ProductDto> getAllProduct();

    Product buyProduct(List<BuyProductDto> buyProductDtos);

   // List<Product> filterItemInStore(String productName, String classify, Integer minPrice, Integer maxPrice, String sort);
}
