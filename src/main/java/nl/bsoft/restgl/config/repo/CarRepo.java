package nl.bsoft.restgl.config.repo;

import nl.bsoft.restgl.model.Vehicle;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@EntityScan("nl.bsoft.restgl.model")
@Repository
public interface CarRepo extends JpaRepository<Vehicle, Integer> {
    List<Vehicle> findByModelCode(@Param("modelCode") String modelCode);
}
