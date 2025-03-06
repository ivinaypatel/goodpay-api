package in.co.goodpay.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RechargeRequestDTO {
    private String senderMobile;
    private String mobileNumber; // Number to recharge
    private BigDecimal amount;
}
