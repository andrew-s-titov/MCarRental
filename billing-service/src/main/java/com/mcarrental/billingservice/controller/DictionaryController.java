package com.mcarrental.billingservice.controller;

import com.mcarrental.billingservice.enums.SwiftCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dictionary/billing")
public class DictionaryController {

    @GetMapping("/swift")
    public ResponseEntity<Enum<?>[]> swift() {
        return ResponseEntity.ok(SwiftCode.values());
    }
}