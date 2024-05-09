package com.crud.crudfrontendbackend.service.product;


import com.crud.crudfrontendbackend.dto.BuyProductDto;
import com.crud.crudfrontendbackend.dto.CreateProductDto;
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

    public Product createProduct(CreateProductDto createProductDto){
        Product existingProduct = productRepository.findbyProductName(createProductDto.getProductName());
        if(existingProduct != null){
            throw new IllegalStateException("Product already exists");
        }
        Product product = new Product();
        BeanUtils.copyProperties(createProductDto, product);
        product.setIsActive(true);
        product.setIsDeleted(false);
        Product newProduct = productRepository.save(product);

        return newProduct;
    }

    //Resolved [org.springframework.web.multipart.support.MissingServletRequestPartException: Required request part 'image' is not present]
//    public Product addProduct(MultipartFile file,ProductDto productDto){
//        Product existingProduct = productRepository.findbyProductName(productDto.getProductName());
//        if(existingProduct != null){
//            throw new IllegalStateException("Product already exists");
//        }
//        try{
//            byte[] data;
//            String fileName;
//
//            if (isBase64Encoded(file)) {
//                // File is base64-encoded
//                data = Base64.getDecoder().decode(getBase64Data(file));
//                fileName = "image.jpg"; // Provide a suitable file name here
//            } else {
//                // File is not base64-encoded
//                data = file.getBytes();
//                fileName = file.getOriginalFilename();
//            }
//
//            String filePath = uploadFolder + "/" + fileName;
//            Files.write(Paths.get(filePath), data);
//
//            Product product = new Product();
//            BeanUtils.copyProperties(productDto, product, "image");
//            product.setImage(filePath);
//            product.setIsDeleted(false);
//            Product newProduct = productRepository.save(product);
//
//            return newProduct;
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
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


    public Product buyProduct(List<BuyProductDto> buyProductDtos) {
        for (BuyProductDto buyProductDto : buyProductDtos) {
            // Product existingSeller = productRepository.findbySeller(buyProductDto.getSeller());
            Product existingProduct = productRepository.findbyProductName(buyProductDto.getProductName());

            // if(existingSeller == null){
            //     throw new IllegalStateException("Seller does not exist");
            // }

            if (existingProduct == null && !existingProduct.getIsDeleted()) {
                throw new IllegalStateException("Product does not exist");
            }

            int availableStock = existingProduct.getStock();
            int requestedStock = buyProductDto.getQuantity();

            if (availableStock < requestedStock) {
                throw new IllegalStateException("Insufficient stock available for purchase");
            }

            existingProduct.setStock(availableStock - requestedStock);

            Product updatedProduct = productRepository.save(existingProduct);
            // Do something with updatedProduct if needed
        }

        return null;
    }


}
