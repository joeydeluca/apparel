package com.apparel.controllers;

import org.springframework.http.HttpStatus;

/**
 * Created by Mick on 4/16/2016.
 */
public class ErrorResponse {
    public final int statusCode;
    public final String msg;

    public ErrorResponse(HttpStatus statusCode, String msg) {
        this.statusCode = statusCode.value();
        this.msg = msg;
    }


}
