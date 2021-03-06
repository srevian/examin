package model;

import javafx.beans.property.SimpleStringProperty;
import java.io.Serializable;

/**
 * POJO class for Student entity.
 *
 * @author Avik Sarkar
 */

public class Student extends Batch implements Serializable {

    private SimpleStringProperty firstName;
    private SimpleStringProperty middleName;
    private SimpleStringProperty lastName ;
    private SimpleStringProperty dob;
    private SimpleStringProperty gender;
    private SimpleStringProperty regYear;
    private SimpleStringProperty email;
    private SimpleStringProperty address;
    private SimpleStringProperty motherName;
    private SimpleStringProperty guardianContactNo;
    private SimpleStringProperty regId;
    private SimpleStringProperty rollNo;
    private SimpleStringProperty contactNo;
    private SimpleStringProperty guardianName;
    private SimpleStringProperty currSemester;


    public Student(){
        this.firstName = new SimpleStringProperty("");
        this.middleName= new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.dob = new SimpleStringProperty("");
        this.gender = new SimpleStringProperty("");
        this.regYear = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.motherName = new SimpleStringProperty("");
        this.guardianContactNo = new SimpleStringProperty("");
        this.regId = new SimpleStringProperty("");
        this.rollNo = new SimpleStringProperty("");
        this.contactNo = new SimpleStringProperty("");
        this.guardianName = new SimpleStringProperty("");
        this.currSemester = new SimpleStringProperty("");
    }


    public String getFirstName() {
        return this.firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getMiddleName() {
        return this.middleName.get();
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    public String getLastName() {
        return this.lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getDob() {
        return this.dob.get();
    }

    public void setDob(String dob) {
        this.dob.set(dob);
    }

    public String getGender() {
        return this.gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getRegYear() {
        return this.regYear.get();
    }

    public void setRegYear(String regYear) {
        this.regYear.set(regYear);
    }

    public String getEmail() {
        return this.email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getAddress() {
        return this.address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getMotherName() {
        return this.motherName.get();
    }

    public void setMotherName(String motherName) {
        this.motherName.set(motherName);
    }

    public String getGuardianContactNo() {
        return this.guardianContactNo.get();
    }

    public void setGuardianContactNo(String guardianContactNo) {
        this.guardianContactNo.set(guardianContactNo);
    }

    public String getRegId() {
        return regId.get();
    }

    public void setRegId(String regId) {
        this.regId.set(regId);
    }

    public String getRollNo() {
        return rollNo.get();
    }

    public void setRollNo(String rollNo) {
        this.rollNo.set(rollNo);
    }

    public String getContactNo() {
        return contactNo.get();
    }

    public void setContactNo(String contactNo) {
        this.contactNo.set(contactNo);
    }

    public String getGuardianName() {
        return guardianName.get();
    }

    public void setGuardianName(String gName) {
        guardianName.set(gName);
    }

    public String getCurrSemester() {
        return currSemester.get();
    }

    public void setCurrSemester(String currSemester) {
        this.currSemester.set(currSemester);
    }

}
