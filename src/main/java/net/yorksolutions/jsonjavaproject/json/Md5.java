package net.yorksolutions.jsonjavaproject.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Md5 {
    @JsonProperty
    String original;

    @JsonProperty
    String md5;

    public Md5(String original) throws NoSuchAlgorithmException {
        this.original = original;

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(original.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();

        this.md5 = myHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Md5 md51 = (Md5) o;
        return Objects.equals(md5, md51.md5) && Objects.equals(original, md51.original);
    }

    @Override
    public int hashCode() {
        return Objects.hash(md5, original);
    }
}
