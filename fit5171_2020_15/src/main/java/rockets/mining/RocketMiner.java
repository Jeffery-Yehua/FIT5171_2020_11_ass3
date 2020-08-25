package rockets.mining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;
import rockets.util.outcome;
import rockets.util.MapSortUtil;

//import java.math.BigDecimal;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class RocketMiner {
    private static Logger logger = LoggerFactory.getLogger(RocketMiner.class);

    private DAO dao;

    public RocketMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * TODO: to be implemented & tested!
     * Returns the top-k most active rockets, as measured by number of completed launches.
     *
     * @param k the number of rockets to be returned.
     * @return the list of k most active rockets.
     */
    public List<Rocket> mostLaunchedRockets(int k) {
        logger.info("find most active " + k + " rockets");
        Collection<Launch> launches = dao.loadAll(Launch.class);

        Map<Rocket, Integer> map = new HashMap<Rocket, Integer>();
        for (Launch launch : launches) {

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
        return result.keySet().stream().limit(k).collect(Collectors.toList());

    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most reliable launch service providers as measured
     * by percentage of successful launches.
     *
     * @param k the number of launch service providers to be returned.
     * @return the list of k most reliable ones.
     */
    public List<LaunchServiceProvider> mostReliableLaunchServiceProviders(int k) {
        logger.info("find most reliable " + k + " lsp");
        Collection<Launch> launches = dao.loadAll(Launch.class);

        Map<LaunchServiceProvider, outcome> map = new HashMap<>();
        for (Launch launch : launches) {

            if (!map.containsKey(launch.getLaunchServiceProvider()))
                map.put(launch.getLaunchServiceProvider(), new outcome());
            if (launch.getLaunchOutcome() == Launch.LaunchOutcome.SUCCESSFUL)
                map.get(launch.getLaunchServiceProvider()).incrementsuccessful();
            if (launch.getLaunchOutcome() == Launch.LaunchOutcome.FAILED)
                map.get(launch.getLaunchServiceProvider()).incrementfailed();

        }

        return MapSortUtil.sortByValueDesc(map).keySet().stream().limit(k).collect(Collectors.toList());
    }

    /**
     * <p>
     * Returns the top-k most recent launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most recent launches.
     */
    public List<Launch> mostRecentLaunches(int k) {
        logger.info("find most recent " + k + " launches");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchDateComparator = (a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate());
        return launches.stream().sorted(launchDateComparator).limit(k).collect(Collectors.toList());
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the dominant country who has the most launched rockets in an orbit.
     *
     * @param orbit the orbit
     * @return the country who sends the most payload to the orbit
     */
    public String dominantCountry(String orbit) {
        logger.info("find the dominant country who has the most launched rockets in " + orbit + " orbit");
        Collection<Launch> launches = dao.loadAll(Launch.class);

        Map<String, Integer> map = new HashMap<>();
        for (Launch launch : launches) {
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
        return MapSortUtil.sortByValueDesc(map).keySet().stream().limit(1).collect(Collectors.toList()).get(0);
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most expensive launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most expensive launches.
     */
    public List<Launch> mostExpensiveLaunches(int k) {
        logger.info("find most expensive " + k + " launches");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchDateComparator = (a, b) -> -a.getPrice().compareTo(b.getPrice());
        return launches.stream().sorted(launchDateComparator).limit(k).collect(Collectors.toList());
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns a list of launch service provider that has the top-k highest
     * sales revenue in a year.
     *
     * @param k    the number of launch service provider.
     * @param year the year in request
     * @return the list of k launch service providers who has the highest sales revenue.
     */
    public List<LaunchServiceProvider> highestRevenueLaunchServiceProviders(int k, int year) {
        logger.info("find highest Revenue" + k + " lsp");
        Collection<Launch> launches = dao.loadAll(Launch.class);

        Map<LaunchServiceProvider, BigDecimal> map = new HashMap<>();
        for (Launch launch : launches) {
            if (launch.getLaunchDate().getYear() == year) {
                if (!map.containsKey(launch.getLaunchServiceProvider()))
                    map.put(launch.getLaunchServiceProvider(), new BigDecimal("0"));

                map.put(launch.getLaunchServiceProvider(),map.get(launch.getLaunchServiceProvider()).add(launch.getPrice()));

            }
        }

        return MapSortUtil.sortByValueDesc(map).keySet().stream().limit(k).collect(Collectors.toList());
    }
}
