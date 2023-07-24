package com.crud.crudfrontendbackend.controller;


import com.crud.crudfrontendbackend.apiresultmodel.ApiResultModel;
import com.crud.crudfrontendbackend.dto.UserDto;
import com.crud.crudfrontendbackend.dto.UserUpdateDto;
import com.crud.crudfrontendbackend.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResultModel createUser(@RequestBody UserDto userDto) {
        return ApiResultModel.builder()
                .resultData(userService.createUser(userDto))
                .message("Success")
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResultModel getAllUsers() {
        return ApiResultModel.builder()
                .resultData(userService.getALlUsers())
                .message("Success")
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .build();
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ApiResultModel getOneUser(@RequestParam String email) {
        return ApiResultModel.builder()
                .resultData(userService.getOneUser(email))
                .message("Success")
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .build();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResultModel deleteUser(@PathVariable Long id) {
        return ApiResultModel.builder()
                .resultData(userService.deleteUser(id))
                .message("Success")
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .build();
    }

    @PutMapping("/update/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResultModel deleteUser(@PathVariable String email,
                                     @RequestBody UserUpdateDto userUpdateDto) {
        return ApiResultModel.builder()
                .resultData(userService.updateUser(email,userUpdateDto))
                .message("Success")
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .build();
    }
}
