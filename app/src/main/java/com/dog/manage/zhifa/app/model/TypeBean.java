package com.dog.manage.zhifa.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TypeBean {

    private Integer id;
    private String title;

    public TypeBean(Integer id, String title) {
        this.id = id;
        this.title = title;
    }
}
