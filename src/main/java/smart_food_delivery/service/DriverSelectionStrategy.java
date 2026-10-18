
package smart_food_delivery.service;

import java.util.List;
import smart_food_delivery.domain.Driver;

public interface DriverSelectionStrategy {

    Driver selectDriver(List<Driver> drivers);

}

