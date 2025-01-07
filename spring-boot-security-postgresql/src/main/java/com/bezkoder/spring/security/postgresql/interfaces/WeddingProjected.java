package com.bezkoder.spring.security.postgresql.interfaces;

import java.time.LocalDate;
import java.util.UUID; // Certifique-se de importar UUID

public interface WeddingProjected {
    Long getId();
    UUID getUuid();
    String getBrideAndGroomName();
    String getImageUrl();
    LocalDate getWeddingDate();

    void setImageUrl(String s);
}
