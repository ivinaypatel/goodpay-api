package in.co.goodpay.api.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.goodpay.api.dto.BillPaymentRequestDTO;
import in.co.goodpay.api.dto.RechargeRequestDTO;
import in.co.goodpay.api.dto.TransactionHistoryDTO;
import in.co.goodpay.api.dto.TransactionRequestDTO;
import in.co.goodpay.api.dto.TransactionResponseDTO;
import in.co.goodpay.api.entity.Transaction;
import in.co.goodpay.api.entity.Wallet;
import in.co.goodpay.api.exception.InvalidRequestException;
import in.co.goodpay.api.exception.UserNotFoundException;
import in.co.goodpay.api.repository.TransactionRepository;
import in.co.goodpay.api.repository.WalletRepository;
import in.co.goodpay.api.service.PaymentService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TransactionResponseDTO transferMoney(TransactionRequestDTO request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Transfer amount must be greater than zero.");
        }

        Wallet senderWallet = walletRepository.findByUser_MobileNumber(request.getSenderMobile())
                .orElseThrow(() -> new UserNotFoundException("Sender wallet not found."));

        Wallet receiverWallet = walletRepository.findByUser_MobileNumber(request.getReceiverMobile())
                .orElseThrow(() -> new UserNotFoundException("Receiver wallet not found."));

        if (senderWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InvalidRequestException("Insufficient balance.");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(request.getAmount()));
        receiverWallet.setBalance(receiverWallet.getBalance().add(request.getAmount()));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        Transaction transaction = new Transaction();
        transaction.setSenderWallet(senderWallet);
        transaction.setReceiverWallet(receiverWallet);
        transaction.setAmount(request.getAmount());
        transaction.setType("TRANSFER");
        transaction.setStatus("SUCCESS");
        transaction.setCreatedOn(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return modelMapper.map(savedTransaction, TransactionResponseDTO.class);
    }

    @Override
    @Transactional
    public TransactionResponseDTO recharge(RechargeRequestDTO request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Recharge amount must be greater than zero.");
        }

        Wallet senderWallet = walletRepository.findByUser_MobileNumber(request.getSenderMobile())
                .orElseThrow(() -> new UserNotFoundException("Wallet not found."));

        if (senderWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InvalidRequestException("Insufficient balance.");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(senderWallet);

        Transaction transaction = new Transaction();
        transaction.setSenderWallet(senderWallet);
        transaction.setReceiverMobileOrServiceId(request.getMobileNumber());
        transaction.setAmount(request.getAmount());
        transaction.setType("RECHARGE");
        transaction.setStatus("SUCCESS");
        transaction.setCreatedOn(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionResponseDTO.class);
    }

    @Override
    @Transactional
    public TransactionResponseDTO payBill(BillPaymentRequestDTO request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Bill payment amount must be greater than zero.");
        }

        Wallet senderWallet = walletRepository.findByUser_MobileNumber(request.getSenderMobile())
                .orElseThrow(() -> new UserNotFoundException("Wallet not found."));

        if (senderWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InvalidRequestException("Insufficient balance.");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(senderWallet);

        Transaction transaction = new Transaction();
        transaction.setSenderWallet(senderWallet);
        transaction.setReceiverMobileOrServiceId(request.getServiceId());
        transaction.setAmount(request.getAmount());
        transaction.setType("BILL_PAYMENT");
        transaction.setStatus("SUCCESS");
        transaction.setCreatedOn(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionResponseDTO.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransactionHistoryDTO> getTransactionHistory(String mobile, String type, LocalDateTime startDate, LocalDateTime endDate) {
        Wallet wallet = walletRepository.findByUser_MobileNumber(mobile)
                .orElseThrow(() -> new UserNotFoundException("Wallet not found for mobile: " + mobile));

        List<Transaction> transactions = transactionRepository.findTransactionsByFilters(wallet, type, startDate, endDate);

        return transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionHistoryDTO.class))
                .collect(Collectors.toList());
    }
}
