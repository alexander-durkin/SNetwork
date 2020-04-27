package model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Credentials {
    String login;
    String password;

    String first_name;
    String last_name;

    String gender;

    String birth_date;

    String address;
    String info;

    public boolean isEmpty() {
        if (
                (getLogin() == null || getLogin().isEmpty()) &&
                (getPassword() == null || getPassword().isEmpty()) &&
                (getFirst_name() == null || getFirst_name().isEmpty()) &&
                (getLast_name() == null || getLast_name().isEmpty()) &&
                (getGender() == null || getGender().isEmpty()) &&
                (getBirth_date() == null || getBirth_date().isEmpty()) &&
                (getAddress() == null || getAddress().isEmpty()) &&
                (getInfo() == null || getInfo().isEmpty())
        ) {
            return true;
        } else{
            return false;
        }
    }
}
