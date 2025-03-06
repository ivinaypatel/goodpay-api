package in.co.goodpay.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {
    private String name;
    private String companyName;
    private String email;
    private String status; 
}
