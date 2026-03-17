package ma.swiftrent.controller;

import ma.swiftrent.composite.fleet.CarGroup;
import ma.swiftrent.service.FleetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fleet")
public class FleetController {

    private final FleetService fleetService;

    public FleetController(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @GetMapping("/available-count")
    public int getAvailableCarsCount() {
        CarGroup fleet = fleetService.buildFleetTree();
        return fleet.countAvailableCars();
    }
}
