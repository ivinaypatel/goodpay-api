package in.co.goodpay.api.service;

import java.time.LocalDateTime;
import java.util.List;

import in.co.goodpay.api.dto.BillPaymentRequestDTO;
import in.co.goodpay.api.dto.RechargeRequestDTO;
import in.co.goodpay.api.dto.TransactionHistoryDTO;
import in.co.goodpay.api.dto.TransactionRequestDTO;
import in.co.goodpay.api.dto.TransactionResponseDTO;

public interface PaymentService {
    TransactionResponseDTO transferMoney(TransactionRequestDTO request);
    TransactionResponseDTO recharge(RechargeRequestDTO request);
    TransactionResponseDTO payBill(BillPaymentRequestDTO request);
    List<TransactionHistoryDTO> getTransactionHistory(String mobile, String type, LocalDateTime startDate, LocalDateTime endDate);
}
