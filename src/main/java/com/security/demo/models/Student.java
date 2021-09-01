package com.security.demo.models;

public class Student {
    
    private final Integer id;
    private final String studentName;

    /**
     * @param id
     * @param studentName
     */
    public Student(Integer id, String studentName) {
        this.id = id;
        this.studentName = studentName;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the studentName
     */
    public String getStudentName() {
        return studentName;
    }

    
}
