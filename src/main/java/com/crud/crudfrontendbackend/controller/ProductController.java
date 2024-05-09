package com.crud.crudfrontendbackend.controller;


import com.crud.crudfrontendbackend.apiresultmodel.ApiResultModel;
import com.crud.crudfrontendbackend.dto.BuyProductDto;
import com.crud.crudfrontendbackend.dto.CreateProductDto;
import com.crud.crudfrontendbackend.dto.ProductDto;
import com.crud.crudfrontendbackend.dto.ProductUpdateDto;
import com.crud.crudfrontendbackend.repository.ProdCriteriaBuilder;
import com.crud.crudfrontendbackend.service.product.ProductService;
import com.crud.crudfrontendbackend.service.product.forFilters.ProdService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ProdService prodService;


    @PostMapping("/upload/{productName}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResultModel uploadImage(@RequestParam("image")MultipartFile file,@PathVariable String productName ) {
        return ApiResultModel.builder()
                .resultData(productService.uploadImageProduct(file,productName))
                .message("Success")
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .build();
    }

//    @PostMapping(value = "/addproduct", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
//                                                    MediaType.APPLICATION_JSON_VALUE})
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResultModel addProduct(@RequestPart("image")MultipartFile file,@RequestPart ProductDto productDto ) {
//        return ApiResultModel.builder()
//                .resultData(productService.addProduct(file,productDto))
//                .message("Success")
//                .isSuccess(true)
//                .status(HttpStatus.OK.value())
//                .build();
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResultModel createProduct(@RequestBody CreateProductDto createProductDto ) {
        return ApiResultModel.builder()
                .resultData(productService.createProduct(createProductDto))
                .message("Success")
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResultModel getAllProduct(){
        return ApiResultModel.builder()
                .resultData(productService.getAllProduct())
                .message("Success")
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .build();
    }


    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public ApiResultModel getFilterItemInStore(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String[] classify,
            @RequestParam(required = false) String order
    ){
        return ApiResultModel.builder()
                .resultData(prodService.filterItemStore(productName, classify, order))
                .message("Success")
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .build();
    }




    @PutMapping("/update/{productName}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResultModel updateProduct(@PathVariable String productName,@RequestBody ProductUpdateDto productUpdateDto){
        return ApiResultModel.builder()
                .resultData(productService.updateProduct(productName,productUpdateDto))
                .message("Success")
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .build();
    }

    @PutMapping("/buy")
    @ResponseStatus(HttpStatus.OK)
    public ApiResultModel buyProduct(@RequestBody List<BuyProductDto> buyProductDtos){
        return ApiResultModel.builder()
                .resultData(productService.buyProduct(buyProductDtos))
                .message("Success")
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .build();
    }


}
