package nl.bsoft.restgl.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import nl.bsoft.restgl.config.repo.CarRepo;
import nl.bsoft.restgl.model.AppInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    private CarRepo carRepo;
    private PrometheusMeterRegistry prometheusRegistry;
    private Counter appInfoCounter;
    private Timer appInfoTimer;

    @Autowired
    public MyController(final CarRepo carRepo,
                        final PrometheusMeterRegistry prometheusRegistry) {
        this.carRepo = carRepo;
        this.prometheusRegistry = prometheusRegistry;
        this.appInfoCounter = Counter.builder("appinfo")
                //.baseUnit("gegevens")
                .tags("bron", "rest")
                .description("Aantal maal aangeroepen")
                .register(prometheusRegistry);

        this.appInfoTimer = Timer.builder("appinfoTimer")
                .tag("bron", "rest")
                .description("Snelheid verwerking van een aanroep")
                .register(prometheusRegistry);
    }

    @GetMapping("/app-info")
    public ResponseEntity<AppInfo> getAppInfo() {

        AppInfo appInfo = new AppInfo();
        appInfo.setName("MyCounterTest");
        appInfo.setVersion("1.0");
        this.appInfoCounter.increment();

        return new ResponseEntity<AppInfo>(appInfo, HttpStatus.OK);
    }

    @GetMapping("/app-info-time")
    public ResponseEntity<AppInfo> getAppInfoTime() {
        Timer.Sample appInfoSample = Timer.start(this.prometheusRegistry);

        AppInfo appInfo = new AppInfo();
        appInfo.setName("MyCounterTest");
        appInfo.setVersion("1.0");

        double maxid = 1000 * Math.random();
        int maxi = (int) maxid;
        appInfo.setMaxi(maxi);
        double result = 0d;
        for (int i = 0; i < maxi; i++) {
            double maxjd = 1000 * Math.random();
            int maxj = (int) maxjd;
            for (int j = 0; j < maxj; j++) {
                double k = i * j / ((i + j) + 1);
                result += k;
            }
        }

        appInfo.setResult(result);
        appInfoSample.stop(this.appInfoTimer);

        return new ResponseEntity<AppInfo>(appInfo, HttpStatus.OK);
    }

}
