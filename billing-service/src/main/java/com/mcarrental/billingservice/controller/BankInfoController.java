package com.mcarrental.billingservice.controller;

import com.mcarrental.billingservice.dto.bankinfo.BankInfoCreateRequestDTO;
import com.mcarrental.billingservice.dto.bankinfo.BankInfoViewDTO;
import com.mcarrental.billingservice.service.BankInfoService;
import com.mcarrental.billingservice.util.RestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/billing/bank_info")
public class BankInfoController {

    private final BankInfoService bankInfoService;

    @GetMapping
    public ResponseEntity<List<BankInfoViewDTO>> getOwnerBankInfo(@RequestParam("owner") UUID carOwnerId) {
        return ResponseEntity.ok(bankInfoService.getOwnerBankInfo(carOwnerId));
    }

    @GetMapping(RestUtil.UUID_V4_PATH)
    public ResponseEntity<BankInfoViewDTO> getBankInfo(@PathVariable("id") UUID bankInfoId) {
        return ResponseEntity.ok(bankInfoService.getBankInfo(bankInfoId));
    }

    @PostMapping
    public ResponseEntity<BankInfoViewDTO> addBankInfo(@Valid @RequestBody BankInfoCreateRequestDTO newBankInfo) {
        BankInfoViewDTO bankInfo = bankInfoService.addBankInfo(newBankInfo);
        return ResponseEntity.created(URI.create("/billing/bank_info" + "/" + bankInfo.getId())).body(bankInfo);
    }

    @PutMapping(RestUtil.UUID_V4_PATH)
    public void setAsMain(@PathVariable("id") UUID bankInfoId) {
        bankInfoService.setBankInfoAsMain(bankInfoId);
    }

    @DeleteMapping(RestUtil.UUID_V4_PATH)
    public void removeBankInfo(@PathVariable("id") UUID bankInfoId) {
        bankInfoService.removeBankInfo(bankInfoId);
    }
}