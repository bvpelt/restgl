package nl.bsoft.restgl.model;

import lombok.Data;
import lombok.EqualsAndHashCode;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
@Entity(name = "Vehicle")
@Table(name = "CAR")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", nullable = false)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESTGL_01_SEQ")
    //@SequenceGenerator(name = "RESTGL_01_SEQ", sequenceName = "CAR_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESTGL_01_SEQ")
    @SequenceGenerator(name = "RESTGL_01_SEQ", sequenceName = "CAR_ID_SEQ", allocationSize = 1)
    private int id;

    @Column(name = "type")
    private String type;

    @Column(name = "model_code")
    private String modelCode;

    @Column(name = "brand_name", nullable = false)
    private String brandName;

    @Column(name = "launch_date")
    private LocalDate launchDate;
}
