
package smart_food_delivery.service;

import java.util.List;
import smart_food_delivery.domain.Driver;

/**
 * Implements a driver selection strategy based on
 * the shortest distance.
 *
 * This strategy selects the nearest available driver
 * from a list of drivers in the Smart Food Delivery
 * Management System.
 *
 * Implements the DriverSelectionStrategy interface.
 */
public class NearestDriverStrategy implements DriverSelectionStrategy {

    /**
     * Selects the available driver with the shortest distance.
     *
     * Null drivers, unavailable drivers, and drivers
     * with negative distances are ignored.
     *
     * @param drivers the list of drivers to choose from
     * @return the nearest available driver, or null
     *         if no suitable driver is found
     */
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
