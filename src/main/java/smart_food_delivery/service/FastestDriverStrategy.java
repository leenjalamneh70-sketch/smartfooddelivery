
package smart_food_delivery.service;

import java.util.List;
import smart_food_delivery.domain.Driver;

public class FastestDriverStrategy implements DriverSelectionStrategy {

    @Override
    public Driver selectDriver(List<Driver> drivers) {

        Driver fastest = null;

        if (drivers == null) {
            return null;
        }

        for (Driver driver : drivers) {

            if (driver == null || !driver.isAvailable()
                    || driver.getEstimatedMinutes() < 0) {
                continue;
            }

            if (fastest == null ||
                driver.getEstimatedMinutes() < fastest.getEstimatedMinutes()) {
                fastest = driver;
            }
        }

        return fastest;
    }
}
