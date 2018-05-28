package com.college.timetable.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class LectureCapabilities {

    @EmbeddedId
    private LectureCapabilityCompositeKey compositeKey;

    @Override
    public String toString() {
        return "LectureCapabilities{" +
                "compositeKey=" + compositeKey +
                '}';
    }

    public LectureCapabilityCompositeKey getCompositeKey() {
        return compositeKey;
    }

    public void setCompositeKey(LectureCapabilityCompositeKey compositeKey) {
        this.compositeKey = compositeKey;
    }

    public LectureCapabilities() {

    }

    public LectureCapabilities(LectureCapabilityCompositeKey compositeKey) {

        this.compositeKey = compositeKey;
    }
}
