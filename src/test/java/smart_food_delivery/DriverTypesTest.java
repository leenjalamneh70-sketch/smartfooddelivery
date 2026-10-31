
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.Driver;
import smart_food_delivery.domain.BicycleDriver;
import smart_food_delivery.domain.MotorcycleDriver;
import smart_food_delivery.domain.CarDriver;
import smart_food_delivery.domain.ElectricVehicleDriver;

class DriverTypesTest {

    @Test
    void testDriverInformation() {
        Driver driver = new Driver("Ahmad", true, 2.5, 15);

        assertEquals("Ahmad", driver.getName());
        assertTrue(driver.isAvailable());
        assertEquals(2.5, driver.getDistance(), 0.001);
        assertEquals(15, driver.getEstimatedMinutes());

        driver.setAvailable(false);
        assertFalse(driver.isAvailable());

        driver.setAvailable(true);
        assertTrue(driver.isAvailable());
    }

    @Test
    void testDriverBasicEligibility() {
        Driver driver = new Driver("Ahmad", true, 2, 15);

        assertTrue(driver.canDeliver(5, 2));
        assertTrue(driver.canDeliver(0, 1));

        assertFalse(driver.canDeliver(-1, 2));
        assertFalse(driver.canDeliver(5, 0));
        assertFalse(driver.canDeliver(5, -1));
        assertFalse(driver.canDeliver(Double.NaN, 2));
        assertFalse(driver.canDeliver(Double.POSITIVE_INFINITY, 2));
        assertFalse(driver.canDeliver(Double.NEGATIVE_INFINITY, 2));

        driver.setAvailable(false);
        assertFalse(driver.canDeliver(5, 2));
    }

    @Test
    void testBicycleValidDelivery() {
        BicycleDriver driver =
            new BicycleDriver("Ali", true, 2, 15);

        assertTrue(driver.canDeliver(3, 2));
        assertTrue(driver.canDeliver(5, 3));
        assertTrue(driver.canDeliver(0, 1));
    }

    @Test
    void testBicycleDistanceLimit() {
        BicycleDriver driver =
            new BicycleDriver("Ali", true, 2, 15);

        assertFalse(driver.canDeliver(5.1, 2));
        assertFalse(driver.canDeliver(10, 2));
        assertFalse(driver.canDeliver(-1, 2));
    }

    @Test
    void testBicycleOrderSizeLimit() {
        BicycleDriver driver =
            new BicycleDriver("Ali", true, 2, 15);

        assertFalse(driver.canDeliver(3, 4));
        assertFalse(driver.canDeliver(3, 0));
    }

    @Test
    void testBicycleUnavailable() {
        BicycleDriver driver =
            new BicycleDriver("Ali", false, 2, 15);

        assertFalse(driver.canDeliver(3, 2));
    }

    @Test
    void testMotorcycleValidDelivery() {
        MotorcycleDriver driver =
            new MotorcycleDriver("Omar", true, 4, 20);

        assertTrue(driver.canDeliver(10, 5));
        assertTrue(driver.canDeliver(15, 8));
        assertTrue(driver.canDeliver(0, 1));
    }

    @Test
    void testMotorcycleDistanceLimit() {
        MotorcycleDriver driver =
            new MotorcycleDriver("Omar", true, 4, 20);

        assertFalse(driver.canDeliver(15.1, 5));
        assertFalse(driver.canDeliver(20, 5));
        assertFalse(driver.canDeliver(-1, 5));
    }

    @Test
    void testMotorcycleOrderSizeLimit() {
        MotorcycleDriver driver =
            new MotorcycleDriver("Omar", true, 4, 20);

        assertFalse(driver.canDeliver(10, 9));
        assertFalse(driver.canDeliver(10, 0));
    }

    @Test
    void testMotorcycleUnavailable() {
        MotorcycleDriver driver =
            new MotorcycleDriver("Omar", false, 4, 20);

        assertFalse(driver.canDeliver(10, 5));
    }

    @Test
    void testCarValidDelivery() {
        CarDriver driver =
            new CarDriver("Omar", true, 10, 25);

        assertTrue(driver.canDeliver(20, 10));
        assertTrue(driver.canDeliver(50, 30));
        assertTrue(driver.canDeliver(0, 1));
    }

    @Test
    void testCarDistanceLimit() {
        CarDriver driver =
            new CarDriver("Omar", true, 10, 25);

        assertFalse(driver.canDeliver(50.1, 10));
        assertFalse(driver.canDeliver(60, 10));
        assertFalse(driver.canDeliver(-1, 10));
        assertFalse(driver.canDeliver(Double.NaN, 10));
    }

    @Test
    void testCarOrderSizeLimit() {
        CarDriver driver =
            new CarDriver("Omar", true, 10, 25);

        assertFalse(driver.canDeliver(20, 31));
        assertFalse(driver.canDeliver(20, 0));
    }

    @Test
    void testCarUnavailable() {
        CarDriver driver =
            new CarDriver("Omar", false, 10, 25);

        assertFalse(driver.canDeliver(20, 10));
    }

    @Test
    void testElectricVehicleValidDelivery() {
        ElectricVehicleDriver driver =
            new ElectricVehicleDriver(
                "Sara", true, 5, 20, 40
            );

        assertEquals(40, driver.getRemainingRangeKm(), 0.001);
        assertTrue(driver.canDeliver(20, 10));
        assertTrue(driver.canDeliver(40, 30));
        assertTrue(driver.canDeliver(0, 1));
    }

    @Test
    void testElectricVehicleInsufficientBattery() {
        ElectricVehicleDriver driver =
            new ElectricVehicleDriver(
                "Sara", true, 5, 20, 40
            );

        assertFalse(driver.canDeliver(40.1, 10));
        assertFalse(driver.canDeliver(50, 10));
    }

    @Test
    void testElectricVehicleOrderSizeLimit() {
        ElectricVehicleDriver driver =
            new ElectricVehicleDriver(
                "Sara", true, 5, 20, 40
            );

        assertFalse(driver.canDeliver(20, 31));
        assertFalse(driver.canDeliver(20, 0));
    }

    @Test
    void testElectricVehicleUnavailable() {
        ElectricVehicleDriver driver =
            new ElectricVehicleDriver(
                "Sara", false, 5, 20, 40
            );

        assertFalse(driver.canDeliver(20, 10));
    }

    @Test
    void testElectricVehicleInvalidBatteryRange() {
        assertThrows(IllegalArgumentException.class, () ->
            new ElectricVehicleDriver("Sara", true, 5, 20, -1)
        );

        assertThrows(IllegalArgumentException.class, () ->
            new ElectricVehicleDriver(
                "Sara", true, 5, 20, Double.NaN
            )
        );

        assertThrows(IllegalArgumentException.class, () ->
            new ElectricVehicleDriver(
                "Sara", true, 5, 20,
                Double.POSITIVE_INFINITY
            )
        );

        assertThrows(IllegalArgumentException.class, () ->
            new ElectricVehicleDriver(
                "Sara", true, 5, 20,
                Double.NEGATIVE_INFINITY
            )
        );
    }

    @Test
    void testElectricVehicleZeroBatteryRange() {
        ElectricVehicleDriver driver =
            new ElectricVehicleDriver(
                "Sara", true, 5, 20, 0
            );

        assertEquals(0, driver.getRemainingRangeKm(), 0.001);
        assertTrue(driver.canDeliver(0, 1));
        assertFalse(driver.canDeliver(1, 1));
    }

}
