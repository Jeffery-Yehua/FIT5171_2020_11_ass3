package rockets.model;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;

public class Payload extends Entity {
    private String name;
    private String type;
    private String mass;

    private String payloadConstructorNullMessage = "Payload constructor parameters cannot be null or empty";

    public Payload(String name, String type) {
        notBlank(name, payloadConstructorNullMessage);
        notBlank(type, payloadConstructorNullMessage);

        if (name.length() >= 128) {
            throw new IllegalArgumentException("name cannot exceed 128 characters");
        }
        if (type.length() >= 128) {
            throw new IllegalArgumentException("type cannot exceed 128 characters");
        }

        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) throws IllegalArgumentException {
        notBlank(mass, "mass cannot be null or empty");
        if (mass.contains("-")) {
            throw new IllegalArgumentException("mass cannot be a negative value");
        }
        this.mass = mass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payload payload = (Payload) o;
        return Objects.equals(name, payload.name) &&
                Objects.equals(type, payload.type) &&
                Objects.equals(mass, payload.mass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
