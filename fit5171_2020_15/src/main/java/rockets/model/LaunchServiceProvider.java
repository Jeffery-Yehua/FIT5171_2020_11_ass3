package rockets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;
import static org.neo4j.ogm.annotation.Relationship.OUTGOING;

@NodeEntity
@CompositeIndex(properties = {"name", "yearFounded", "country"}, unique = true)
public class LaunchServiceProvider extends Entity {
    @Property(name = "name")
    private String name;

    @Property(name = "yearFounded")
    private int yearFounded;

    @Property(name = "country")
    private String country;

    @Property(name = "headquarters")
    private String headquarters;


    @Relationship(type = "MANUFACTURES", direction= OUTGOING)
    @JsonIgnore
    private Set<Rocket> rockets;

    public LaunchServiceProvider() {
        super();
    }

    public LaunchServiceProvider(String name, int yearFounded, String country) {
        notBlank(name, "Constructor param cannot be null or empty");
        if (name.length() >= 128) {
            throw new IllegalArgumentException("Constructor param cannot exceed 128 characters");
        }

        notNull(yearFounded, "Constructor param cannot be null or empty");
        if (yearFounded <=0 || yearFounded >=3000) {
            throw new IllegalArgumentException("Constructor parameter year cannot be negative or larger than 2999");
        }
        notBlank(country, "Constructor param cannot be null or empty");
        if (country.length() >= 128) {
            throw new IllegalArgumentException("Constructor param cannot exceed 128 characters");
        }

        this.name = name;
        this.yearFounded = yearFounded;
        this.country = country;

        this.rockets = new LinkedHashSet<>();
    }


    public String getName() {
        return name;
    }

    public int getYearFounded() {
        return yearFounded;
    }

    public String getCountry() {
        return country;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public Set<Rocket> getRockets() {
        return rockets;
    }

    public void setHeadquarters(String headquarters) {
        notBlank(headquarters, "Headquarters cannot be null or empty" );
        if (headquarters.length() >= 128) {
            throw new IllegalArgumentException("Headquarters cannot exceed 128 characters");
        }

        this.headquarters = headquarters;
    }

    public void setRockets(Set<Rocket> rockets) {
        notNull(rockets, "Rockets Set cannot be null");


        this.rockets = rockets;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaunchServiceProvider that = (LaunchServiceProvider) o;
        return yearFounded == that.yearFounded &&
                Objects.equals(name, that.name) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, yearFounded, country);
    }

    @Override
    public String toString() {
        return "Launch Service Provider{" + '\n' +
                "name='" + name + '\'' + '\n' +
                ", country='" + country + '\'' +
                ", year founded='" + yearFounded + '\'' +'\n' +
                ", headquarter='" + headquarters + '\'' +
                '}' + '\n' ;
    }
}
