package nl.bsoft.restgl.config.repo;

import nl.bsoft.restgl.model.Garage;
import nl.bsoft.restgl.model.Vehicle;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarageRepo extends JpaRepository<Garage, Integer> {
    List<Garage> findByAdresCity(@Param("city") String city);
}
