package in.co.goodpay.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillPaymentRequestDTO {
    private String senderMobile;
    private String serviceId; // Electricity, Water, Gas service ID
    private BigDecimal amount;
}
