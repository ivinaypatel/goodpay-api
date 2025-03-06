package in.co.goodpay.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import in.co.goodpay.api.common.BaseEntity;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private BigDecimal balance;


}
