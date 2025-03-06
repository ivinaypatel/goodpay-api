package in.co.goodpay.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryDTO {
    private int transactionId;
    private String senderMobile;
    private String receiverMobileOrServiceId;
    private BigDecimal amount;
    private String type;
    private String status;
    private LocalDateTime timestamp;
}
