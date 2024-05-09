package application.models;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private double value;

    @Enumerated(EnumType.STRING)
    @Column(name = "month")
    private Month month;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcc_id", nullable = true)
    private Mcc mcc;
}
