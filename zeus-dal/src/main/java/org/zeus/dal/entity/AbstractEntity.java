package org.zeus.dal.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public abstract class AbstractEntity implements Serializable {
    private String id;

    public void generateId() {
        id = UUID.randomUUID().toString();
    }
}
