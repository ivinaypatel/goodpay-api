package in.co.goodpay.api.service.impl;

import in.co.goodpay.api.dto.WalletResponseDTO;
import in.co.goodpay.api.entity.Wallet;
import in.co.goodpay.api.exception.UserNotFoundException;
import in.co.goodpay.api.exception.InvalidRequestException;
import in.co.goodpay.api.repository.UserRepository;
import in.co.goodpay.api.repository.WalletRepository;
import in.co.goodpay.api.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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
}
