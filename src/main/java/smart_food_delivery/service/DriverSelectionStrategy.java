
package smart_food_delivery.service;

import java.util.List;
import smart_food_delivery.domain.Driver;

/**
 * Defines a strategy for selecting a delivery driver
 * in the Smart Food Delivery Management System.
 *
 * Different implementations can select drivers
 * based on different criteria, such as distance
 * or estimated delivery time.
 */
public interface DriverSelectionStrategy {

    /**
     * Selects a delivery driver from a given list
     * according to the implemented selection strategy.
     *
     * @param drivers the list of drivers to choose from
     * @return the selected driver, or null if no
     *         suitable driver is found
     */
    Driver selectDriver(List<Driver> drivers);

}
