package com.crud.crudfrontendbackend.service.product;


import com.crud.crudfrontendbackend.dto.ProductDto;
import com.crud.crudfrontendbackend.dto.ProductUpdateDto;
import com.crud.crudfrontendbackend.model.Product;
import com.crud.crudfrontendbackend.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    private String uploadFolder =  "D:/cpe/crud/images";

    public List<ProductDto> getAllProduct(){
        List<Product> products = productRepository.findAllNotDeletedProduct();
        return products.stream().map(this::convertToDtoWithBase64Image).collect(Collectors.toList());
    }

    private ProductDto convertToDtoWithBase64Image(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto, "image"); // Exclude the "image" field from copying

        String imageFilePath = product.getImage();
        if (imageFilePath != null) {
            try {
                // Read the image file from the file system
                byte[] imageData = Files.readAllBytes(Paths.get(imageFilePath));

                // Convert image data to Base64
                String base64Image = Base64.getEncoder().encodeToString(imageData);
                productDto.setImage(base64Image);
            } catch (IOException e) {
                // Handle the exception if the image file cannot be read
                e.printStackTrace();
            }
        } else {
            // Set a default image or skip image processing, depending on your requirement
            // For example:
            productDto.setImage("data:image/png;base64,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");
        }

        return productDto;
    }

    public Product uploadImageProduct(MultipartFile file, String productName){

        Product existingProduct = productRepository.findbyProductName(productName);
        if(existingProduct == null){
            throw new IllegalStateException("Product does not exists");
        }
        try{
            byte[] data;
            String fileName;

            if (isBase64Encoded(file)) {
                // File is base64-encoded
                data = Base64.getDecoder().decode(getBase64Data(file));
                fileName = "image.jpg"; // Provide a suitable file name here
            } else {
                // File is not base64-encoded
                data = file.getBytes();
                fileName = file.getOriginalFilename();
            }

            String filePath = uploadFolder + "/" + fileName;
            Files.write(Paths.get(filePath), data);

            existingProduct.setImage(filePath);
            Product savedProduct = productRepository.save(existingProduct);
            return savedProduct;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Product createProduct(ProductDto productDto){
        Product existingProduct = productRepository.findbyProductName(productDto.getProductName());
        if(existingProduct != null){
            throw new IllegalStateException("Product already exists");
        }
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        product.setIsDeleted(false);
        Product newProduct = productRepository.save(product);

        return newProduct;
    }

    public Product addProduct(MultipartFile file,ProductDto productDto){
        Product existingProduct = productRepository.findbyProductName(productDto.getProductName());
        if(existingProduct != null){
            throw new IllegalStateException("Product already exists");
        }
        try{
            byte[] data;
            String fileName;

            if (isBase64Encoded(file)) {
                // File is base64-encoded
                data = Base64.getDecoder().decode(getBase64Data(file));
                fileName = "image.jpg"; // Provide a suitable file name here
            } else {
                // File is not base64-encoded
                data = file.getBytes();
                fileName = file.getOriginalFilename();
            }

            String filePath = uploadFolder + "/" + fileName;
            Files.write(Paths.get(filePath), data);

            Product product = new Product();
            BeanUtils.copyProperties(productDto, product, "image");
            product.setImage(filePath);
            product.setIsDeleted(false);
            Product newProduct = productRepository.save(product);

            return newProduct;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean isBase64Encoded(MultipartFile file) {
        return file.getContentType() != null && file.getContentType().startsWith("data:image/");
    }
    private String getBase64Data(MultipartFile file) throws IOException {
        return IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8).split(",")[1];
    }

    public Product updateProduct(String productName,ProductUpdateDto productUpdateDto) {
        Product existingProduct = productRepository.findbyProductName(productName);
        if (existingProduct == null) {
            throw new IllegalStateException("Product does not exists");
        }
        BeanUtils.copyProperties(productUpdateDto, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return updatedProduct;
    }

}
