package in.co.goodpay.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.goodpay.api.dto.BillPaymentRequestDTO;
import in.co.goodpay.api.dto.RechargeRequestDTO;
import in.co.goodpay.api.dto.TransactionHistoryDTO;
import in.co.goodpay.api.dto.TransactionRequestDTO;
import in.co.goodpay.api.dto.TransactionResponseDTO;
import in.co.goodpay.api.service.PaymentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/goodpay-api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transferMoney(@RequestBody TransactionRequestDTO request) {
        return ResponseEntity.ok(paymentService.transferMoney(request));
    }

    @PostMapping("/recharge")
    public ResponseEntity<TransactionResponseDTO> recharge(@RequestBody RechargeRequestDTO request) {
        return ResponseEntity.ok(paymentService.recharge(request));
    }

    @PostMapping("/bill-payment")
    public ResponseEntity<TransactionResponseDTO> payBill(@RequestBody BillPaymentRequestDTO request) {
        return ResponseEntity.ok(paymentService.payBill(request));
    }
    
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionHistoryDTO>> getTransactionHistory(
            @RequestParam String mobile,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        return ResponseEntity.ok(paymentService.getTransactionHistory(mobile, type, startDate, endDate));
    }
}
