package testinput.login.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="transactions")
public class Transactions {
    @Id
    private Integer id; 

    private String Date;
    private String Description;
    private String Amount;
    private String Balance;
}
  