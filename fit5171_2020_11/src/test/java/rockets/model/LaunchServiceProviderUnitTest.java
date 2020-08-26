package rockets.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LaunchServiceProviderUnitTest {
    private LaunchServiceProvider target;


    @BeforeEach
    public void setUp() {

        target = new LaunchServiceProvider("NASA", 1958, "USA");

    }

    // CONSTRUCTOR

    //valid values
    @DisplayName("should produce the same object with identical parameter")
    @Test
    public void ShouldProduceTheSameObject(){
        LaunchServiceProvider dopperganger  = new LaunchServiceProvider("NASA", 1958, "USA");
        assertEquals(dopperganger, target);

    }

    @DisplayName("should throw exception when pass a empty string to ")
    @ParameterizedTest
    @CsvSource({"'', 1958, USA", "' ',1958, USA", "'  ',1958, USA" ,
                            "NASA, 1958, ''" ,    "NASA, 1958, ' '", "NASA, 1958,'   '"})
    public void shouldThrowExceptionWhenConstructorArgumentIsEmpty(String name , int year , String country){

        //target = new LaunchServiceProvider(name , year , country );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LaunchServiceProvider(name , year , country ));
        assertEquals("Constructor param cannot be null or empty", exception.getMessage());

    }

    @DisplayName("should throw null exception when pass a null as parameter ")
    @ParameterizedTest
    @CsvSource({", 1958, USA",
            "NASA, 1958, " })
    public void shouldThrowNullExceptionWhenConstructorArgumentIsEmpty(String name , int year , String country){

        //target = new LaunchServiceProvider(name , year , country );
        assertThrows(NullPointerException.class, () -> new LaunchServiceProvider(name , year , country ));

    }



    @DisplayName("should throw exception when pass invalid year number ")
    @ParameterizedTest
    @CsvSource({"NASA, -1, USA", "NASA, 0, USA", "NASA, 3000, USA"})
    public void shouldThrowExceptionWhenYearNumberIsInvalid(String name , int year , String country){

        //target = new LaunchServiceProvider(name , year , country );
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LaunchServiceProvider(name , year , country ));
        assertEquals("Constructor parameter year cannot be negative or larger than 2999", exception.getMessage());

    }

    @DisplayName("should throw exception when pass a name longer than 128 characters to the Constructor")
    @ParameterizedTest
    @CsvSource({"'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque pen', 1958, USA",
            "NASA, 1958, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque pen'" })
    public void shouldThrowExceptionWhenConstructorParamExceed128Characters(String name , int year , String country) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new LaunchServiceProvider(name , year , country ));
        assertEquals("Constructor param cannot exceed 128 characters", exception.getMessage());


    }


    //HEADQUARTERS

    //Behavioral Test
    @DisplayName("Should get the name that was input using the setter ")
    @ParameterizedTest
    @ValueSource(strings = {"USA", "USSR", "EU"})
    void setHeadquartersTest(String headquarters) {
        target.setHeadquarters(headquarters);
        assertEquals(headquarters, target.getHeadquarters());

    }


    @DisplayName("should throw exception when pass a empty string to setHeadquarters function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhensetHeadquartersToEmpty(String Headquarters) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setHeadquarters(Headquarters));
        assertEquals("Headquarters cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setHeadquarters function")
    @Test
    public void shouldThrowExceptionWhensetHeadquartersToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setHeadquarters(null));
        assertEquals("Headquarters cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a name longer than 128 characters to setHeadquarters function")
    @ParameterizedTest
    @ValueSource(strings = {"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque pen"})
    public void shouldThrowExceptionWhenHeadquartersExceeds128Characters(String Headquarters) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setHeadquarters(Headquarters));
        assertEquals("Headquarters cannot exceed 128 characters", exception.getMessage());


    }

    //ROCKETS
    @DisplayName("The set was set and the stored set must be identical")
    @Test
    void ShouldGetBackTheSameSetUsingSetRockets() {

        //LaunchServiceProvider testManufacturer = new LaunchServiceProvider("NASA", 1958, "USA");


        Rocket rocketA = new Rocket("A", "USA", target);
        Rocket rocketB = new Rocket("B", "USA", target);
        Rocket rocketC = new Rocket("C", "EU", target);
        Set<Rocket> testSet = new HashSet<Rocket>();
        testSet.add(rocketA);
        testSet.add(rocketB);
        testSet.add(rocketC);
        target.setRockets(testSet);
        assertEquals(testSet, target.getRockets());

    }

    @DisplayName("should throw exception when pass null to setRockets function")
    @Test
    public void shouldThrowExceptionWhenSetRocketsToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setRockets(null));
        assertEquals("Rockets Set cannot be null", exception.getMessage());
    }

    //EQUALS FUNCTION TESTING
    @DisplayName("should return true for identical objects")
    @Test
    void ShouldReturnTrueForIdenticalObjects() {
        LaunchServiceProvider doublegangerA = new LaunchServiceProvider("NASA", 1958, "USA");
        LaunchServiceProvider doublegangerB = new LaunchServiceProvider("NASA", 1958, "USA");
        assertEquals(true, doublegangerA.equals(doublegangerB));

    }

    //EQUALS FUNCTION TESTING
    @DisplayName("should return false for different objects")
    @Test
    void ShouldReturnFalseForDifferentObjects() {
        LaunchServiceProvider doublegangerA = new LaunchServiceProvider("NASA", 1958, "USA");
        LaunchServiceProvider doublegangerB = new LaunchServiceProvider("UME", 2009, "BOSHI");
        assertEquals(false, doublegangerA.equals(doublegangerB));

    }

    //HASH CODE FUNCTION TESTING
    @DisplayName("should hash equal value with equal parameter")
    @Test
    void ShouldHashEqualParametersToEqualHash() {
        LaunchServiceProvider testA = new LaunchServiceProvider("NASA", 1958, "USA");
        LaunchServiceProvider testB = new LaunchServiceProvider("NASA", 1958, "USA");
        assertEquals(testA, testB);
    }

    @DisplayName("should hash different value with different parameter")
    @ParameterizedTest
    @CsvSource({ "NASI, 1958, USA" ,"NASA, 1959, USA" , "NASA, 1958, EU" , "NAS, 195, US"  })
    void ShouldHashNotEqualParametersToUnequalHash(String name , int year , String country) {
        LaunchServiceProvider testA = new LaunchServiceProvider("NASA", 1958, "USA");
        LaunchServiceProvider testB = new LaunchServiceProvider(name, year, country);
        assertNotEquals(testA, testB);
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