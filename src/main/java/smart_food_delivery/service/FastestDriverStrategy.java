
package smart_food_delivery.service;

import java.util.List;
import smart_food_delivery.domain.Driver;

/**
 * Implements a driver selection strategy based on
 * the shortest estimated delivery time.
 *
 * This strategy selects the fastest available driver
 * from a list of drivers in the Smart Food Delivery
 * Management System.
 *
 * Implements the DriverSelectionStrategy interface.
 */
public class FastestDriverStrategy implements DriverSelectionStrategy {

    /**
     * Selects the available driver with the shortest
     * estimated delivery time.
     *
     * Drivers who are null, unavailable, or have
     * a negative estimated delivery time are ignored.
     *
     * @param drivers the list of drivers to choose from
     * @return the fastest available driver, or null
     *         if no suitable driver is found
     */
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
