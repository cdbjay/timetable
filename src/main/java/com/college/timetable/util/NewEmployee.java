package com.college.timetable.util;

public class NewEmployee {
    private Double id;
    private String firstName;
    private String lastName;


    public NewEmployee(){}

    public NewEmployee(Double id, String firstName, String lastName) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return "NewEmployee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
