package in.co.goodpay.api.service;

import in.co.goodpay.api.dto.WalletResponseDTO;
import java.math.BigDecimal;

public interface WalletService {
    WalletResponseDTO getWalletByMobile(String mobileNumber);
    WalletResponseDTO deposit(String mobileNumber, BigDecimal amount);
    WalletResponseDTO withdraw(String mobileNumber, BigDecimal amount);
}
