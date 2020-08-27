//package rockets.model;
//
//import org.neo4j.ogm.annotation.NodeEntity;
//
//import java.util.Objects;
//
//import static org.apache.commons.lang3.Validate.notBlank;
//
//@NodeEntity
//public class User extends Entity {
//    private String firstName;
//
//    private String lastName;
//
//    private String email;
//
//    private String password;
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) throws IllegalArgumentException {
//        notBlank(firstName, "firstName cannot be null or empty");
//        if (firstName.length() >= 128) {
//            throw new IllegalArgumentException("firstName cannot exceed 128 characters");
//        }
//        this.firstName = firstName;
//    }
//
//    public String getLastName() { return lastName; }
//
//    public void setLastName(String lastName) throws IllegalArgumentException {
//        notBlank(lastName, "lastName cannot be null or empty");
//        if (lastName.length() >= 64) {
//            throw new IllegalArgumentException("lastName cannot exceed 64 characters");
//        }
//        this.lastName = lastName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) throws IllegalArgumentException {
//        notBlank(email, "email cannot be null or empty");
//        if (email.length() >= 320 && email.contains("@")) {
//            throw new IllegalArgumentException("email cannot exceed 320 characters");
//        } else if (email.length() <= 5 && email.contains("@")){
//            throw new IllegalArgumentException("email cannot be shorter than 5 characters");
//        }
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) throws IllegalArgumentException {
//        notBlank(password, "password cannot be null or empty");
//        if (password.length() >= 128) {
//            throw new IllegalArgumentException("password cannot exceed 128 characters");
//        }
//        else if (password.length() <= 6) {
//            throw new IllegalArgumentException("password cannot be shorter than 6 characters");
//        }
//        this.password = password;
//    }
//
//    // match the given password against user's password and return the result
//    public boolean isPasswordMatch(String password) {
//        return this.password.equals(password.trim());
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        User user = (User) o;
//        return Objects.equals(email, user.email);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(email);
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", email='" + email + '\'' +
//                '}';
//    }
//}
package rockets.model;

import java.util.Objects;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.Validate.*;

public class User extends Entity {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        notBlank(lastName, "lastname cannot be null or empty");
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        notBlank(email, "email cannot be null or empty");
        String regex = "^(.+)@(.+)$";
        matchesPattern(email,regex,"email must be valid");
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        notBlank(password, "password cannot be null or empty");
        this.password = password;
    }

    // match the given password against user's password and return the result
    public boolean isPasswordMatch(String password) {
        return this.password.equals(password.trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
