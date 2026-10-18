
package smart_food_delivery.service;

import java.util.List;
import smart_food_delivery.domain.Driver;

public class NearestDriverStrategy implements DriverSelectionStrategy {

    @Override
    public Driver selectDriver(List<Driver> drivers) {

        Driver nearest = null;

        if (drivers == null) {
            return null;
        }

        for (Driver driver : drivers) {

            if (driver == null || !driver.isAvailable()
                    || driver.getDistance() < 0) {
                continue;
            }

            if (nearest == null ||
                driver.getDistance() < nearest.getDistance()) {
                nearest = driver;
            }
        }

        return nearest;
    }
}
