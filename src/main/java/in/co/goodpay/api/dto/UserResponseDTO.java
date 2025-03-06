package in.co.goodpay.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private int id;
    private String name;
    private String mobileNumber;
    private String companyName;
    private String email;
    private String role;
    private String status;
}
