package com.epam.rd.java.basic.practice8.db;

public class DBConstants {
    private DBConstants(){}

    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    private static final String INSERT_INTO = "INSERT INTO ";
    private static final String WHERE = " WHERE ";
    private static final String ID = "id";
    private static final String LOGIN = "login";
    private static final String NAME = "name";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_TEAMS = "teams";
    public static final String TABLE_USERS_TEAMS = "users_teams";
    public static final String TEAM_ID = "team_id";
    public static final String USER_ID = "user_id";
    public static final String FIND_ALL_USERS = SELECT_ALL_FROM + TABLE_USERS;
    public static final String FIND_ALL_TEAMS = SELECT_ALL_FROM + TABLE_TEAMS;
    public static final String INSERT_USER = INSERT_INTO + TABLE_USERS + " VALUES(DEFAULT ,?)";
    public static final String INSERT_TEAM = INSERT_INTO + TABLE_TEAMS + " VALUES(DEFAULT ,?)";
    public static final String INSERT_USER_TO_TEAM = INSERT_INTO + TABLE_USERS_TEAMS + " VALUES(?,?)";
    public static final String GET_USER_TEAMS_ID = "SELECT " + TEAM_ID + " FROM " + TABLE_USERS_TEAMS + WHERE + USER_ID + "=?";
    public static final String GET_TEAM = SELECT_ALL_FROM + TABLE_TEAMS + WHERE + NAME + "=?";
    public static final String GET_USER = SELECT_ALL_FROM + TABLE_USERS + WHERE + LOGIN + "=?";
    public static final String DELETE_TEAM_BY_NAME = "DELETE FROM " + TABLE_TEAMS + WHERE + NAME + "=?";
    public static final String UPDATE_TEAM = "UPDATE " + TABLE_TEAMS + " SET " + NAME + "=? WHERE " + ID + "=?";

}
