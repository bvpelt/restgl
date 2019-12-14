package nl.bsoft.restgl.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode
@Entity(name = "Garage")
@Table(name = "GARAGE")
public class Garage implements Serializable {
    private static final long serialVersionUID = 2L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESTGL_02_SEQ")
    @SequenceGenerator(name = "RESTGL_02_SEQ", sequenceName = "GARAGE_ID_SEQ", allocationSize = 1)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "street")
    private String adresStreet;

    @Column(name = "streetnumber")
    private String adresStreetNumber;

    @Column(name = "postcode")
    private String adresStreetPostCode;

    @Column(name = "city")
    private String adresCity;

    @Column(name = "phonenumber")
    private String adresTelephoneNumber;
    ;

}
