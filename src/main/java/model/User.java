package model;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

@Value //делает поля private final, делает геттеры, конструкторы, кастует параметры
@Builder
public class User implements Serializable {
    String id;
    String passwordHash; //scrypt для мужиков, md5 для обычных пацанов

    String firstName;
    String lastName;

    Gender gender;

    Timestamp birthDate;

    String address;

    String info;

}
