package in.co.goodpay.api.controller;

import in.co.goodpay.api.dto.WalletResponseDTO;
import in.co.goodpay.api.service.WalletService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/goodpay-api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    // Get Wallet by User Mobile Number
    @GetMapping("/{mobileNumber}")
    public ResponseEntity<WalletResponseDTO> getWalletByMobile(@PathVariable String mobileNumber) {
        return ResponseEntity.ok(walletService.getWalletByMobile(mobileNumber));
    }

    // Deposit Money into Wallet
    @PostMapping("/{mobileNumber}/deposit")
    public ResponseEntity<WalletResponseDTO> deposit(
            @PathVariable String mobileNumber,
            @RequestParam @NotNull BigDecimal amount) {

        return ResponseEntity.ok(walletService.deposit(mobileNumber, amount));
    }

    // Withdraw Money from Wallet
    @PostMapping("/{mobileNumber}/withdraw")
    public ResponseEntity<WalletResponseDTO> withdraw(
            @PathVariable String mobileNumber,
            @RequestParam @NotNull BigDecimal amount) {

        return ResponseEntity.ok(walletService.withdraw(mobileNumber, amount));
    }
}
