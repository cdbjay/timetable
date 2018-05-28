package com.college.timetable.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class ClassCapacity {

    @EmbeddedId
    private ClassCapacityCompositeKey compositeKey;
    private Integer capacity;

    public ClassCapacity(ClassCapacityCompositeKey compositeKey, Integer capacity) {
        this.compositeKey = compositeKey;
        this.capacity = capacity;
    }

    public ClassCapacity() {
    }

    @Override
    public String toString() {
        return "ClassCapacity{" +
                "compositeKey=" + compositeKey +
                ", capacity=" + capacity +
                '}';
    }

    public ClassCapacityCompositeKey getCompositeKey() {
        return compositeKey;
    }

    public void setCompositeKey(ClassCapacityCompositeKey compositeKey) {
        this.compositeKey = compositeKey;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
