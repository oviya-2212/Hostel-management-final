package com.hostel.model;

/**
 * Student domain entity: Holds student details and contact information.
 * Demonstrates Unit 1 (Classes, Encapsulation, Constructors, Overloading)
 * and Unit 2 (Inheritance from BaseEntity, Method Overriding).
 */
public class Student extends BaseEntity {
    private String name;
    private String department;
    private int year;
    private String phone;
    private String email;

    // Overloaded Constructor 1
    public Student(int studentId, String name, String department, int year, String phone, String email) {
        super(studentId);
        this.name = name;
        this.department = department;
        this.year = year;
        this.phone = phone;
        this.email = email;
    }

    // Overloaded Constructor 2 (Minimal)
    public Student(int studentId, String name, String department) {
        this(studentId, name, department, 1, "0000000000", name.toLowerCase().replace(" ", "") + "@univ.edu");
    }

    public int getStudentId() {
        return getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getSummary() {
        return String.format("Student[ID=%d, Name='%s', Dept='%s', Year=%d]", id, name, department, year);
    }

    @Override
    public String toString() {
        return String.format("%-6d %-18s %-8s Year-%-2d %-12s %-25s", id, name, department, year, phone, email);
    }
}
