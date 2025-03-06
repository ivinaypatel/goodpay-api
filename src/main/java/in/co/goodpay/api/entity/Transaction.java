package in.co.goodpay.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import in.co.goodpay.api.common.BaseEntity;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_wallet_id", nullable = false)
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "receiver_wallet_id")
    private Wallet receiverWallet; 
    
    @Column(nullable = true)
    private String receiverMobileOrServiceId; // Mobile number for recharge, service provider ID for bills

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String type; 

   
}
