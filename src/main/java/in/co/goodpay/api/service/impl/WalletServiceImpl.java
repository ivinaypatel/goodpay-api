package in.co.goodpay.api.service.impl;

import in.co.goodpay.api.dto.WalletResponseDTO;
import in.co.goodpay.api.entity.Transaction;
import in.co.goodpay.api.entity.Wallet;
import in.co.goodpay.api.exception.UserNotFoundException;
import in.co.goodpay.api.exception.InvalidRequestException;
import in.co.goodpay.api.repository.TransactionRepository;
import in.co.goodpay.api.repository.UserRepository;
import in.co.goodpay.api.repository.WalletRepository;
import in.co.goodpay.api.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final TransactionRepository transactionRepository;

    @Override
    public WalletResponseDTO getWalletByMobile(String mobileNumber) {
        Wallet wallet = walletRepository.findByUser_MobileNumber(mobileNumber)
                .orElseThrow(() -> new UserNotFoundException("Wallet not found for user: " + mobileNumber));

        return modelMapper.map(wallet, WalletResponseDTO.class);
    }

    @Override
    @Transactional
    public WalletResponseDTO deposit(String mobileNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Deposit amount must be greater than zero.");
        }

        Wallet wallet = walletRepository.findByUser_MobileNumber(mobileNumber)
                .orElseThrow(() -> new UserNotFoundException("Wallet not found for user: " + mobileNumber));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        return modelMapper.map(wallet, WalletResponseDTO.class);
    }

    @Override
    @Transactional
    public WalletResponseDTO withdraw(String mobileNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Withdrawal amount must be greater than zero.");
        }

        Wallet wallet = walletRepository.findByUser_MobileNumber(mobileNumber)
                .orElseThrow(() -> new UserNotFoundException("Wallet not found for user: " + mobileNumber));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InvalidRequestException("Insufficient balance.");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        return modelMapper.map(wallet, WalletResponseDTO.class);
    }

	@Override
	@Transactional
	public WalletResponseDTO transferMoney(String senderMobile, String receiverMobile, BigDecimal amount) {
		
		 if (amount.compareTo(BigDecimal.ZERO) <= 0) {
		        throw new InvalidRequestException("Withdrawal amount must be greater than zero.");
		    }

		    Wallet senderWallet = walletRepository.findByUser_MobileNumber(senderMobile)
		            .orElseThrow(() -> new UserNotFoundException("Sender wallet not found for user: " + senderMobile));

		    Wallet receiverWallet = walletRepository.findByUser_MobileNumber(receiverMobile)
		            .orElseThrow(() -> new UserNotFoundException("Receiver wallet not found for user: " + receiverMobile));

		    if (senderWallet.getBalance().compareTo(amount) < 0) {
		        throw new InvalidRequestException("Insufficient balance.");
		    }

		    // Deduct money from sender
		    senderWallet.setBalance(senderWallet.getBalance().subtract(amount));

		    // Credit money to receiver
		    receiverWallet.setBalance(receiverWallet.getBalance().add(amount));

		    // Save updated balances
		    walletRepository.save(senderWallet);
		    walletRepository.save(receiverWallet);

		    // Record the transaction
		    Transaction transaction = new Transaction();
		    transaction.setSenderWallet(senderWallet);
		    transaction.setReceiverWallet(receiverWallet);
		    transaction.setAmount(amount);
		    transaction.setType("TRANSFER");
		    transaction.setStatus("SUCCESS");
		    transaction.setCreatedOn(LocalDateTime.now());

		    transactionRepository.save(transaction);

		    return modelMapper.map(senderWallet, WalletResponseDTO.class);
	}
}
