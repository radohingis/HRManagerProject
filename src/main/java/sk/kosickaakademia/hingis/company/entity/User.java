package sk.kosickaakademia.hingis.company.entity;

import sk.kosickaakademia.hingis.company.enumerator.Gender;

public class User {
    private int id;
    private String fname;
    private String lname;
    private int age;
    private Gender gender;

    public User(String fname, String lname, int age, int gender) {
        this.fname = fname;
        this.lname = lname;
        this.age = age;
        this.gender = gender == 0 ? Gender.male : gender == 1 ? Gender.female : Gender.other;
    }

    public User(int id, String fname, String lname, int age, int gender) {
        this(fname, lname, age, gender);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public String stringify() {
        return getId() + " " + getFname() + " " + getLname() + " " + getAge() + " " + getGender();
    }
}
