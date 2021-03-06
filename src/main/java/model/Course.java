package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Course entity.
 *
 * @author Avik Sarkar
 */
public class Course extends Department {

    private SimpleStringProperty discipline;
    private SimpleStringProperty degree ;
    private SimpleStringProperty courseId;
    private SimpleStringProperty duration;
    private SimpleStringProperty deptName;

    public Course(){
        this.discipline = new SimpleStringProperty("");
        this.degree = new SimpleStringProperty("");
        this.courseId = new SimpleStringProperty("");
        this.duration = new SimpleStringProperty("");
        this.deptName = new SimpleStringProperty("");
    }


    public String getDiscipline(){
        return this.discipline.get();
    }

    public void setDiscipline(String discipline) {
        this.discipline.set(discipline);
    }

    public String getDegree(){
        return this.degree.get();
    }

    public void setDegree(String degree) {
        this.degree.set(degree);
    }

    public String getCourseId(){
        return this.courseId.get();
    }

    public void setCourseId(String courseId) {
        this.courseId.set(courseId);
    }

    public String getDuration(){
        return this.duration.get();
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }

    public String getDeptName(){
        return this.deptName.get();
    }

    public void setDeptName(String deptName) {
        this.deptName.set(deptName);
    }

}

