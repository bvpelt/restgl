package nl.bsoft.restgl.controller;

import nl.bsoft.restgl.config.repo.CarRepo;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private CarRepo carRepo;

    @Autowired
    public RestController(final CarRepo carRepo) {
        this.carRepo = carRepo;
    }


}
