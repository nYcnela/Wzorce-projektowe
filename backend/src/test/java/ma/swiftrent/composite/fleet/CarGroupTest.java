package ma.swiftrent.composite.fleet;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/*
    Tydzień 13, Testy jednostkowe 3
    Testy jednostkowe dla kompozytu, które sprawdzają jego metody
    oraz rekurencję kompozytu
 */
class CarGroupTest {

    @Test
    void shouldReturnGroupName() {
        CarGroup group = new CarGroup("SUV");

        String result = group.getName();

        assertThat(result).isEqualTo("SUV");
    }

    @Test
    void shouldAddChildComponent() {
        CarGroup group = new CarGroup("Fleet");
        CarComponent car = mock(CarComponent.class);

        group.add(car);

        assertThat(group.getChildren()).containsExactly(car);
    }

    @Test
    void shouldRemoveChildComponent() {
        CarGroup group = new CarGroup("Fleet");
        CarComponent car = mock(CarComponent.class);

        group.add(car);

        group.remove(car);

        assertThat(group.getChildren()).isEmpty();
    }

    @Test
    void shouldCountAvailableCarsFromChildren() {
        CarGroup group = new CarGroup("Fleet");

        CarComponent firstCar = mock(CarComponent.class);
        CarComponent secondCar = mock(CarComponent.class);

        when(firstCar.countAvailableCars()).thenReturn(1);
        when(secondCar.countAvailableCars()).thenReturn(1);

        group.add(firstCar);
        group.add(secondCar);

        int result = group.countAvailableCars();

        assertThat(result).isEqualTo(2);

        verify(firstCar).countAvailableCars();
        verify(secondCar).countAvailableCars();
    }

    @Test
    void shouldCountAvailableCarsFromNestedGroups() {
        CarGroup mainGroup = new CarGroup("Main fleet");
        CarGroup cityGroup = new CarGroup("City cars");
        CarGroup suvGroup = new CarGroup("SUV cars");

        CarComponent availableCar = mock(CarComponent.class);
        CarComponent unavailableCar = mock(CarComponent.class);
        CarComponent anotherAvailableCar = mock(CarComponent.class);

        when(availableCar.countAvailableCars()).thenReturn(1);
        when(unavailableCar.countAvailableCars()).thenReturn(0);
        when(anotherAvailableCar.countAvailableCars()).thenReturn(1);

        cityGroup.add(availableCar);
        cityGroup.add(unavailableCar);
        suvGroup.add(anotherAvailableCar);

        mainGroup.add(cityGroup);
        mainGroup.add(suvGroup);

        int result = mainGroup.countAvailableCars();

        assertThat(result).isEqualTo(2);

        verify(availableCar).countAvailableCars();
        verify(unavailableCar).countAvailableCars();
        verify(anotherAvailableCar).countAvailableCars();
    }
}
//Koniec, Tydzień 13, Testy jednostkowe 3