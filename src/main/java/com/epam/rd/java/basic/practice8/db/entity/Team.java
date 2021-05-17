package com.epam.rd.java.basic.practice8.db.entity;

public class Team {
    private String teamName;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Team(){}

    public Team(String name){
        this.teamName = name;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public int hashCode() {
        return teamName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (obj.getClass().isInstance(this)){
            Team team = (Team) obj;
            return team.getTeamName().equals(this.getTeamName());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return teamName;
    }

    public static Team createTeam(String teamName){
        return new Team(teamName);
    }
}
