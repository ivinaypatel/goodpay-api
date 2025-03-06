package in.co.goodpay.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String name;
    private String mobileNumber;
    private String companyName;
    private String email;
    private String role; // USER or ADMIN
    private String status;
}
