package rockets.mining;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.dataaccess.neo4j.Neo4jDAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;
import rockets.util.outcome;
import rockets.util.MapSortUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RocketMinerUnitTest {
    Logger logger = LoggerFactory.getLogger(RocketMinerUnitTest.class);

    private DAO dao;
    private RocketMiner miner;
    private List<Rocket> rockets;
    private List<LaunchServiceProvider> lsps;
    private List<Launch> launches;
    private List<Launch.LaunchOutcome> launchOutcome;

    private List<String> orbits;

    private List<BigDecimal> prices;

    @BeforeEach
    public void setUp() {
        dao = mock(Neo4jDAO.class);
        miner = new RocketMiner(dao);
        rockets = Lists.newArrayList();

        lsps = Arrays.asList(
                new LaunchServiceProvider("ULA", 1990, "USA"),
                new LaunchServiceProvider("SpaceX", 2002, "USA"),
                new LaunchServiceProvider("ESA", 1975, "Europe")
        );
        launchOutcome = Arrays.asList(Launch.LaunchOutcome.SUCCESSFUL, Launch.LaunchOutcome.FAILED);

        orbits = Arrays.asList("LEO", "GTO", "Others");

        prices = Arrays.asList(new BigDecimal("87"), new BigDecimal("23"), new BigDecimal("15"));

        //index of outcome of each launch
        int[] outcomeIndex = new int[]{0, 1, 1, 1, 0, 1, 1, 0, 0, 1};

        // index of lsp of each rocket
        int[] lspIndex = new int[]{0, 0, 0, 1, 1};
        // 5 rockets
        for (int i = 0; i < 5; i++) {
            rockets.add(new Rocket("rocket_" + i, "USA", lsps.get(lspIndex[i])));
        }
        // month of each launch
        int[] months = new int[]{1, 6, 4, 3, 4, 11, 6, 5, 12, 5};

        // index of rocket of each launch
        int[] rocketIndex = new int[]{0, 0, 0, 0, 1, 1, 1, 2, 2, 3};


        // index of lsp of each rocket
        int[] lspIndexForLaunch = new int[]{0, 2, 0, 1, 1, 0, 2, 1, 1, 0};

        //index of orbit of each launch
        int[] OrbitIndexForLaunch = new int[]{2, 0, 1, 1, 0, 2, 1, 0, 1, 2};

        //index of prices for each Launch

        int[] priceIndexForLaunch = new int[]{0, 2, 0, 0, 1, 1, 1, 2, 2, 1};

        // 10 launches
        launches = IntStream.range(0, 10).mapToObj(i -> {
            logger.info("create " + i + " launch in month: " + months[i]);
            Launch l = new Launch();
            l.setLaunchDate(LocalDate.of(2017, months[i], 1));
            l.setLaunchVehicle(rockets.get(rocketIndex[i]));
            l.setLaunchSite("VAFB");
            l.setOrbit(orbits.get(OrbitIndexForLaunch[i]));
            l.setLaunchServiceProvider(lsps.get(lspIndexForLaunch[i]));
            l.setLaunchOutcome(launchOutcome.get(outcomeIndex[i]));
            l.setPrice(prices.get(priceIndexForLaunch[i]));
            spy(l);
            return l;
        }).collect(Collectors.toList());
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostRecentLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate()));
        List<Launch> loadedLaunches = miner.mostRecentLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostActiveRockets(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);

        List<Launch> sortedLaunches = new ArrayList<>(launches);
        Map<Rocket, Integer> map = new HashMap<Rocket, Integer>();
        for (Launch launch : sortedLaunches) {

            if (!map.containsKey(launch.getLaunchVehicle()))
                map.put(launch.getLaunchVehicle(), 1);
            else {

                int tmp = map.get(launch.getLaunchVehicle()).intValue();
                tmp++;

                map.put(launch.getLaunchVehicle(), Integer.valueOf(tmp));
            }


        }
        Map<Rocket, Integer> result = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        List<Rocket> sortedRockets = result.keySet().stream().limit(k).collect(Collectors.toList());
        List<Rocket> loadedRockets = miner.mostLaunchedRockets(k);
        assertEquals(k, loadedRockets.size());
        assertEquals(sortedRockets, loadedRockets);
        System.out.println(loadedRockets);
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostReliableLsp(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        Map<LaunchServiceProvider, outcome> map = new HashMap<>();
        for (Launch launch : sortedLaunches) {

            if (!map.containsKey(launch.getLaunchServiceProvider()))
                map.put(launch.getLaunchServiceProvider(), new outcome());
            if (launch.getLaunchOutcome() == Launch.LaunchOutcome.SUCCESSFUL)
                map.get(launch.getLaunchServiceProvider()).incrementsuccessful();
            if (launch.getLaunchOutcome() == Launch.LaunchOutcome.FAILED)
                map.get(launch.getLaunchServiceProvider()).incrementfailed();
        }

        List<LaunchServiceProvider> lsp = MapSortUtil.sortByValueDesc(map).keySet().stream().limit(k).collect(Collectors.toList());
        List<LaunchServiceProvider> loadedLsp = miner.mostReliableLaunchServiceProviders(k);
        assertEquals(k, loadedLsp.size());
        assertEquals(lsp, loadedLsp);
        for (LaunchServiceProvider p : lsp) System.out.println(p.getName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"LEO", "GTO", "Others"})
    public void shouldReturnDominantCountryInGivenOrbit(String orbit) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        Map<String, Integer> map = new HashMap<>();
        for (Launch launch : sortedLaunches) {
            if (launch.getOrbit().equals(orbit)) {
                if (!map.containsKey(launch.getLaunchServiceProvider().getCountry()))
                    map.put(launch.getLaunchServiceProvider().getCountry(), 1);
                else {

                    int tmp = map.get(launch.getLaunchServiceProvider().getCountry()).intValue();
                    tmp++;

                    map.put(launch.getLaunchServiceProvider().getCountry(), Integer.valueOf(tmp));
                }

            }
        }
        String ExpectedCountry = MapSortUtil.sortByValueDesc(map).keySet().stream().limit(1).collect(Collectors.toList()).get(0);
        String country = miner.dominantCountry(orbit);
        System.out.println(country);

        assertEquals(ExpectedCountry, country);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void shouldReturnTopMostExpensiveLaunches(int k) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> sortedLaunches = new ArrayList<>(launches);
        sortedLaunches.sort((a, b) -> -a.getPrice().compareTo(b.getPrice()));
        List<Launch> loadedLaunches = miner.mostExpensiveLaunches(k);
        assertEquals(k, loadedLaunches.size());
        assertEquals(sortedLaunches.subList(0, k), loadedLaunches);
    }

    @ParameterizedTest
    @MethodSource("createParameters")
    public void shouldReturnHighestRevenueLsp(int k, int year) {
        when(dao.loadAll(Launch.class)).thenReturn(launches);
        List<Launch> NewLaunches = new ArrayList<>(launches);
        Map<LaunchServiceProvider, BigDecimal> map = new HashMap<>();
        for (Launch launch : NewLaunches) {
            if (launch.getLaunchDate().getYear() == year) {
                if (!map.containsKey(launch.getLaunchServiceProvider()))
                    map.put(launch.getLaunchServiceProvider(), new BigDecimal("0"));

                map.put(launch.getLaunchServiceProvider(),map.get(launch.getLaunchServiceProvider()).add(launch.getPrice()));

            }
        }

        List<LaunchServiceProvider> Expected=MapSortUtil.sortByValueDesc(map).keySet().stream().limit(k).collect(Collectors.toList());
        List<LaunchServiceProvider> loadedLsp = miner.highestRevenueLaunchServiceProviders(k, year);
        assertEquals(k, loadedLsp.size());
        assertEquals(Expected, loadedLsp);

    }

    private static Stream<Arguments> createParameters() {
        return Stream.of(
                Arguments.of(1, 2017),
                Arguments.of(2, 2017),
                Arguments.of(3, 2017)
        );
    }

}