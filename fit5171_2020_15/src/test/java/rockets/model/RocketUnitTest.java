package rockets.model;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
public class RocketUnitTest {


    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    private String name = "Falcon 9";
    private String country = "United States";
    private LaunchServiceProvider manufacturer = new LaunchServiceProvider("NASA", 1958, "USA");

    @DisplayName("should create rocket successfully when given right parameters to constructor")
    @Test
    public void shouldConstructRocketObject() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertNotNull(bfr);
    }

    @DisplayName("should throw exception when given null manufacturer to constructor")
    @Test
    public void shouldThrowExceptionWhenNoManufacturerGiven() {
        String name = "BFR";
        String country = "USA";
        assertThrows(NullPointerException.class, () -> new Rocket(name, country, null));
    }

    @DisplayName("should set rocket massToLEO value")
    @ValueSource(strings = {"10000", "15000"})
    public void shouldSetMassToLEOWhenGivenCorrectValue(String massToLEO) {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");

        Rocket bfr = new Rocket(name, country, manufacturer);

        bfr.setMassToLEO(massToLEO);
        assertEquals(massToLEO, bfr.getMassToLEO());
    }

    @DisplayName("should throw exception when set massToLEO to null")
    @Test
    public void shouldThrowExceptionWhenSetMassToLEOToNull() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertThrows(NullPointerException.class, () -> bfr.setMassToLEO(null));
    }


    class RocketConstructorTest {
        final String longerThan128Characters = "morbi leo urna molestie at elementum eu facilisis sed odio morbi quis commodo odio aenean sed adipiscing diam donec adipiscing tristique risus nec feugiat in fermentum posuere urna nec tincidunt praesent semper";

        String rocketConstructorNullMessage = "Rocket constructor parameters cannot be null or empty";

        @DisplayName("should throw NullPointerException when pass null parameters in Rocket constructor")
        @Test
        @Parameters (method = "parametersForshouldThrowNullPointerExceptionWhenPassNullInRocketConstructor")

        public void shouldThrowNullPointerExceptionWhenPassNullInRocketConstructor(String name, String country, LaunchServiceProvider manufacturer) {
            System.out.println("name:\t\t" + name);
            System.out.println("country:\t\t" + country);
            System.out.println("manufacturer:\t\t" + manufacturer);
            System.out.println("==========================================");
            NullPointerException exception = assertThrows(NullPointerException.class, () -> new Rocket(name, country, manufacturer));
            assertEquals(rocketConstructorNullMessage, exception.getMessage());
        }
        private  Object[] parametersForshouldThrowNullPointerExceptionWhenPassNullInRocketConstructor() {
            LaunchServiceProvider manufacturer = new LaunchServiceProvider("NASA", 1958, "USA");
            return new Object[]{
                    new Object[]{ null , "country", manufacturer},
                    new Object[]{ "name" , null, manufacturer},
                    new Object[]{ "name" , "country", null},

            };
        }

        @DisplayName("should throw IllegalArgumentException when pass blank parameters in Rocket constructor")
        @Test
        @Parameters (method = "parametersForshouldThrowIllegalArgumentExceptionWhenPassBlankInRocketConstructor")

        public void shouldThrowIllegalArgumentExceptionWhenPassBlankInRocketConstructor(String name, String country, LaunchServiceProvider manufacturer) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Rocket(name, country, manufacturer));
            assertEquals(rocketConstructorNullMessage, exception.getMessage());
        }
        private  Object[] parametersForshouldThrowIllegalArgumentExceptionWhenPassBlankInRocketConstructor() {
            LaunchServiceProvider manufacturer = new LaunchServiceProvider("NASA", 1958, "USA");
            return new Object[]{
                    new Object[]{ "" , "country", manufacturer},
                    new Object[]{ " " , "country", manufacturer},
                    new Object[]{ "  " , "country", manufacturer},
                    new Object[]{ "name" , "", manufacturer},
                    new Object[]{ "name" , " ", manufacturer},
                    new Object[]{ "name" , "  ", manufacturer}
            };
        }


        @DisplayName("should throw IllegalArgumentException when pass a name longer than 128 characters to Rocket constructor")
        @ParameterizedTest
        @ValueSource(strings = {longerThan128Characters})
        public void shouldThrowIllegalArgumentExceptionWhenNameExceeds128Characters(String longName) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Rocket(longName, country, manufacturer));
            assertEquals("name cannot exceed 128 characters", exception.getMessage());
        }

        @DisplayName("should throw IllegalArgumentException when pass a country longer than 128 characters to Rocket constructor")
        @ParameterizedTest
        @ValueSource(strings = {longerThan128Characters})
        public void shouldThrowIllegalArgumentExceptionWhenCountryExceeds128Characters(String longCountry) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Rocket(name, longCountry, manufacturer));
            assertEquals("country cannot exceed 128 characters", exception.getMessage());
        }

    }



    @Nested
    @DisplayName("Test for boundary cases in setters for orbital mass")
    class RocketBoundaryTest {
        private Rocket target;

        @BeforeEach
        public void setUp() {

            target = new Rocket(name, country, manufacturer);
        }

        // setMassToLEO

        @DisplayName("should throw exception when pass an empty string to setMassToLEO function")
        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  "})
        public void shouldThrowExceptionWhenSetMassToLEOToEmpty(String massToLEO) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToLEO(massToLEO));
            assertEquals("massToLEO cannot be null or empty", exception.getMessage());
        }

        @DisplayName("should throw exception when pass null to setMassToLEO function")
        @Test
        public void shouldThrowExceptionWhenSetMassToLEOToNull() {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToLEO(null));
            assertEquals("massToLEO cannot be null or empty", exception.getMessage());
        }

        @DisplayName("should throw exception when pass an empty string to setMassToLEO function")
        @ParameterizedTest
        @ValueSource(strings = {"-1", "-0.1", "-0.01"})
        public void shouldThrowExceptionWhenSetMassToLEOToNegativeValue(String massToLEO) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToLEO(massToLEO));
            assertEquals("massToLEO cannot be a negative value", exception.getMessage());
        }

        // setMassToGTO

        @DisplayName("should throw exception when pass an empty string to setMassToGTO function")
        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  "})
        public void shouldThrowExceptionWhenSetMassToGTOToEmpty(String massToGTO) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToGTO(massToGTO));
            assertEquals("massToGTO cannot be null or empty", exception.getMessage());
        }

        @DisplayName("should throw exception when pass null to setMassToGTO function")
        @Test
        public void shouldThrowExceptionWhenSetMassToGTOToNull() {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToGTO(null));
            assertEquals("massToGTO cannot be null or empty", exception.getMessage());
        }

        @DisplayName("should throw exception when pass an empty string to setMassToGTO function")
        @ParameterizedTest
        @ValueSource(strings = {"-1", "-0.1", "-0.01"})
        public void shouldThrowExceptionWhenSetMassToGTOToNegativeValue(String massToGTO) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToGTO(massToGTO));
            assertEquals("massToGTO cannot be a negative value", exception.getMessage());
        }

        // setMassToOther

        @DisplayName("should throw exception when pass an empty string to setMassToOther function")
        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  "})
        public void shouldThrowExceptionWhenSetMassToOtherToEmpty(String massToOther) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToOther(massToOther));
            assertEquals("massToOther cannot be null or empty", exception.getMessage());
        }

        @DisplayName("should throw exception when pass null to setMassToOther function")
        @Test
        public void shouldThrowExceptionWhenSetMassToOtherToNull() {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToOther(null));
            assertEquals("massToOther cannot be null or empty", exception.getMessage());
        }

        @DisplayName("should throw exception when pass an empty string to setMassToOther function")
        @ParameterizedTest
        @ValueSource(strings = {"-1", "-0.1", "-0.01"})
        public void shouldThrowExceptionWhenSetMassToOtherToNegativeValue(String massToOther) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToOther(massToOther));
            assertEquals("massToOther cannot be a negative value", exception.getMessage());
        }

       /* @DisplayName("should throw exception when request family of Rocket without family")
        @Test
        public void shouldThrowExceptionWhenRequestFamilyOfRocketWithoutFamily() {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> target.getModel());
            assertEquals("cannot get family for rocket without a parent", exception.getMessage());
        }*/
    }

    @DisplayName("should hash equivalent Rocket parameters to equal hash values")
    @Test
    public void shouldHashEquivalentRocketParametersToEqualHashValues() {
        Rocket x = new Rocket(name, country, manufacturer);
        Rocket y = new Rocket(name, country, manufacturer);
        assertEquals(x.hashCode(), y.hashCode());
    }

    @Nested
    @DisplayName("Test for equality of rocket objects")
    class RocketEqualityTest {
        private Rocket target;

        @BeforeEach
        public void setUp() {
            target = new Rocket(name, country, manufacturer);
        }

        @DisplayName("should return true when two rockets have the same parameters")
        @Test
        public void shouldReturnTrueWhenRocketsHaveSameParameters() {
            Rocket anotherRocket = new Rocket(name, country, manufacturer);
            assertTrue(target.equals(anotherRocket));
        }

        @DisplayName("should return false when two rockets have different parameters")
        @Test
        public void shouldReturnFalseWhenRocketsHaveDifferentParameters() {
            Rocket anotherRocket = new Rocket("Not " + name, country, manufacturer);
            assertFalse(target.equals(anotherRocket));
        }
    }

    @AfterAll
    static void freeExternalResources() {
        System.out.println("Freeing external resources...");
    }





}
