package testinput.login.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="checking")
public class Checking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  

    private String balance;
    private String availableBalance;  
    
   // @OneToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "user_id", nullable = false)
    //private User user;
}
