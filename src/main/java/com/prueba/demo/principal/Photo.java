package com.prueba.demo.principal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class Photo {
    @JsonProperty("thumb")
    private String thumb;

    @JsonProperty("highres")
    private String highres;

    @JsonProperty("is_user_uploaded")
    private boolean isUserUploaded;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getHighres() {
        return highres;
    }

    public void setHighres(String highres) {
        this.highres = highres;
    }

    public boolean isUserUploaded() {
        return isUserUploaded;
    }

    public void setUserUploaded(boolean userUploaded) {
        this.isUserUploaded = userUploaded;
    }
}