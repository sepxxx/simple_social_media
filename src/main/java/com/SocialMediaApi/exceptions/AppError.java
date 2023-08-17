package com.SocialMediaApi.exceptions;


import lombok.Data;

import java.util.Date;

@Data
public class AppError {
    private int status;
    private String message;
    private Date date;

    public AppError(int status, String message) {
        this.status = status;
        this.message = message;
        this.date = new Date();
    }
}
