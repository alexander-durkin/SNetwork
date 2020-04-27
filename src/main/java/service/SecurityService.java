package service;

public interface SecurityService {
    String encript(String password);
    boolean validate(String password, String hash);
}
