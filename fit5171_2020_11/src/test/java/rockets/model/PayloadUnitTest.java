package rockets.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PayloadUnitTest {
    private Payload target;

    private String name = "Optus 10";
    private String type = "Satellite";

    @Nested
    @DisplayName("Test for constructors")
    class PayloadConstructorTest {
        final String longerThan128Characters = "morbi leo urna molestie at elementum eu facilisis sed odio morbi quis commodo odio aenean sed adipiscing diam donec adipiscing tristique risus nec feugiat in fermentum posuere urna nec tincidunt praesent semper";

        String payloadConstructorNullMessage = "Payload constructor parameters cannot be null or empty";

        @DisplayName("should throw NullPointerException when pass null parameters in Payload constructor")
        @ParameterizedTest
        @CsvSource({
                ",",
                ",type",
                "name,"
        })
        public void shouldThrowNullPointerExceptionWhenPassNullInPayloadConstructor(String name, String type) {
            System.out.println("name:\t\t" + name);
            System.out.println("country:\t\t" + type);
            System.out.println("==========================================");
            NullPointerException exception = assertThrows(NullPointerException.class, () -> new Payload(name, type));
            assertEquals(payloadConstructorNullMessage, exception.getMessage());
        }


        @DisplayName("should throw IllegalArgumentException when pass blank parameters in Payload constructor")
        @ParameterizedTest
        @CsvSource({
                "'',''",
                "'',type",
                "name,''",
                "' ',' '",
                "' ',type",
                "name,' '",
                "'  ','  '",
                "'  ',type",
                "name,'  '"
        })
        public void shouldThrowIllegalArgumentExceptionWhenPassBlankInPayloadConstructor(String name, String type) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Payload(name, type));
            assertEquals(payloadConstructorNullMessage, exception.getMessage());
        }

        @DisplayName("should throw IllegalArgumentException when pass a name longer than 128 characters to Payload constructor")
        @ParameterizedTest
        @ValueSource(strings = {longerThan128Characters})
        public void shouldThrowIllegalArgumentExceptionWhenNameExceeds128Characters(String longName) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Payload(longName, type));
            assertEquals("name cannot exceed 128 characters", exception.getMessage());
        }

        @DisplayName("should throw IllegalArgumentException when pass a type longer than 128 characters to Payload constructor")
        @ParameterizedTest
        @ValueSource(strings = {longerThan128Characters})
        public void shouldThrowIllegalArgumentExceptionWhenTypeExceeds128Characters(String longType) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Payload(name, longType));
            assertEquals("type cannot exceed 128 characters", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Test for boundary cases")
    class PayloadBoundaryTest {
        @BeforeEach
        void setUp() {
            target = new Payload(name, type);
        }

        @DisplayName("should throw NullPointerException when set mass to null")
        @Test
        void shouldThrowNullPointerExceptionWhenSetMassToNull() {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMass(null));
            assertEquals("mass cannot be null or empty", exception.getMessage());
        }

        @DisplayName("should throw IllegalArgumentException when set mass to empty")
        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  "})
        void shouldThrowIllegalArgumentExceptionWhenSetMassToEmpty(String mass) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMass(mass));
            assertEquals("mass cannot be null or empty", exception.getMessage());
        }

        @DisplayName("should throw IllegalArgumentException when set mass to negative value")
        @ParameterizedTest
        @ValueSource(strings = {"-1", "-0.1", "-0.01"})
        void shouldThrowIllegalArgumentExceptionWhenSetMassToNegativeValue(String mass) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMass(mass));
            assertEquals("mass cannot be a negative value", exception.getMessage());
        }
    }

    @DisplayName("should hash equivalent Payload parameters to equal hash values")
    @Test
    public void shouldHashEquivalentPayloadParametersToEqualHashValues() {
        Payload x = new Payload(name, type);
        Payload y = new Payload(name, type);
        assertEquals(x.hashCode(), y.hashCode());
    }

    @Nested
    @DisplayName("Test for equality and hash")
    class PayloadEqualityTest {
        @BeforeEach
        void setUp() {
            target = new Payload(name, type);
        }

        @DisplayName("should return true when two payloads have the same parameters")
        @Test
        public void shouldReturnTrueWhenRocketsHaveSameParameters() {
            Payload anotherPayload = new Payload(name, type);
            assertTrue(target.equals(anotherPayload));
        }

        @DisplayName("should return false when two payloads have different parameters")
        @Test
        public void shouldReturnFalseWhenRocketsHaveDifferentParameters() {
            Payload anotherPayload = new Payload("Not " + name, type);
            assertFalse(target.equals(anotherPayload));
        }
    }

    @AfterEach
    void tearDown() {
        System.out.println("Tearing down...");
    }

    @AfterAll
    static void freeExternalResources() {
        System.out.println("Freeing external resources...");
    }
}