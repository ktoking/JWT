package com.chilly.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by Chilly Cui on 2020/9/9.
 */
@Data
@Accessors(chain = true)
public class User {
    private String id;
    private String name;
    private String password;
}
