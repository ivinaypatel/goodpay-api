package in.co.goodpay.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseDTO {
    private int id;
    private String userMobileNumber;
    private BigDecimal balance;
    private String status;
}
