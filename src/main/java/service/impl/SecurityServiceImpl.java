package service.impl;

import service.SecurityService;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityServiceImpl implements SecurityService {
    @Override
    public String encript(String password) {
        final Charset charset = Charset.forName("UTF-8");
        try {
            return new String(MessageDigest.getInstance("MD5").
                    digest(password.getBytes(charset)), charset);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); //нужны свои исключения
        }
    }

    @Override
    public boolean validate(String password, String hash) {
        //несоленый MD5
        //return hash.equals(encript(password));
        return hash.equals(password);
    }
}
