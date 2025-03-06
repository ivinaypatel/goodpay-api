package in.co.goodpay.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    private String senderMobile;
    private String receiverMobile;
    private BigDecimal amount;
}
