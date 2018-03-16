package ereferences.example.com;

/**
 * Created by solan on 14-02-18.
 */

public class StudentModel {

    public String fullname;
    public String email;
    public String password;
    public String enrollment;
    public String mobile;
    public String department;


    public StudentModel() {
    }


    public StudentModel(String fullname, String email, String password, String enrollment, String mobile, String department) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.enrollment = enrollment;
        this.mobile = mobile;
        this.department = department;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


}
