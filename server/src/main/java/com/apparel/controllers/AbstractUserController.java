package com.apparel.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * All end points based off users extend this.
 *
 * Created by Mick on 4/17/2016.
 */
@RestController
@RequestMapping("/users")
public abstract class AbstractUserController {
}
