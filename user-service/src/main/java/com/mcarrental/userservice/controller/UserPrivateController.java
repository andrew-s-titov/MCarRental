package com.mcarrental.userservice.controller;

import com.mcarrental.userservice.service.UserService;
import com.mcarrental.userservice.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserPrivateController {

    private final UserService userService;

    @GetMapping("/email" + RestUtil.UUID_V4_PATH)
    public ResponseEntity<String> getEmailByUserId(@PathVariable("id") UUID userId) {
        return ResponseEntity.ok(userService.getEmailById(userId));
    }
}