package nl.bsoft.restgl.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.NamedAttributeNode;
import java.io.Serializable;

@Slf4j
@Data
public class AppInfo implements Serializable {
    private static final long serialVersionUID = 3L;

    private String name;
    private String version;


}
