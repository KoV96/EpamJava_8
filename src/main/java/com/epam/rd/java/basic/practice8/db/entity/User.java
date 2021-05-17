package com.epam.rd.java.basic.practice8.db.entity;



public class User {
    private int id;
    private String login;

    public User(){}

    public User(String login){
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return this.login;
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (obj.getClass().isInstance(this)){
            User user = (User) obj;
            return this.getLogin().equals(user.getLogin());
        } else {
            return false;
        }
    }

    public static User createUser(String login){
        return new User(login);
    }
}
