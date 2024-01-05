package testinput.login.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; 

    private String Date;
    private String Description;
    private String Amount;
    private String Balance;
    private String time;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn( name = "userID")
    private User user;
    
}
  