package com.epam.rd.java.basic.practice8.db;

import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class DBManager {
    private static final Logger LOGGER = Logger.getLogger(DBManager.class.getName());
    private static final String URL = "connection.url";
    private static final String URL_PATH = "app.properties";
    private static DBManager dbManager;

    private DBManager() {
    }

    public static synchronized DBManager getInstance() {
        if (dbManager == null){
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public Connection getConnection(String connectionUrl) throws SQLException {
        try(FileInputStream fio = new FileInputStream(URL_PATH)){
            Properties properties = new Properties();
            properties.load(fio);
            return DriverManager.getConnection(properties.getProperty(connectionUrl));
        } catch (SQLException | IOException e){
            LOGGER.severe(e.getMessage());
        }
        return null;
    }

    public List<User> findAllUsers(){
        List<User> users = new ArrayList<>();
        try(Connection connection = getConnection(URL);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(DBConstants.FIND_ALL_USERS)){
            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setLogin(resultSet.getString("login"));
                users.add(user);
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return users;
    }

    public void insertUser(User user){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try(Connection connection = getConnection(URL)){
            ps = connection.prepareStatement(DBConstants.INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getLogin());
            if (ps.executeUpdate() != 1){
                return;
            }
            rs = ps.getGeneratedKeys();
            if (rs.next()){
                int id = rs.getInt(1);
                user.setId(id);
            }
        } catch (SQLException e) {
            LOGGER.severe("insert user : " + e.getMessage());
        } finally {
            close(rs);
            close(ps);
        }
    }

    public void insertTeam(Team team){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try(Connection connection = getConnection(URL)){
            ps = connection.prepareStatement(DBConstants.INSERT_TEAM, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, team.getTeamName());
            if (ps.executeUpdate() != 1){
                return;
            }
            rs = ps.getGeneratedKeys();
            if (rs.next()){
                int id = rs.getInt(1);
                team.setId(id);
            }

        } catch (SQLException e) {
            LOGGER.severe("insert team : " + e.getMessage());
        } finally {
            close(rs);
            close(ps);
        }
    }

    public List<Team> findAllTeams(){
        List<Team> teams = new ArrayList<>();
        try(Connection connection = getConnection(URL);
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(DBConstants.FIND_ALL_TEAMS)) {
            while (rs.next()){
                Team team = new Team();
                team.setName(rs.getString("name"));
                team.setId(rs.getInt("id"));
                teams.add(team);
            }
        } catch (SQLException e) {
            LOGGER.severe("Finding teams : " + e.getMessage());
        }
        return teams;
    }

    public void setTeamsForUser(User user, Team... teams){
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = getConnection(URL);
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(DBConstants.INSERT_USER_TO_TEAM);
            for (Team team : teams){
                ps.setInt(1, user.getId());
                ps.setInt(2, team.getId());
                ps.addBatch();
            }
            int[] userTeams = ps.executeBatch();
            for (int i : userTeams){
                if (i != 1){
                    return;
                }
            }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                LOGGER.severe(ex.getMessage());
            }
        } finally {
            close(ps);
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.severe(e.getMessage());
            }
        }
    }

    public List<Team> getUserTeams(User user){
        List<Team> teams = new ArrayList<>();
        List<Integer> teamsId = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try(Connection connection = getConnection(URL)){
            ps = connection.prepareStatement(DBConstants.GET_USER_TEAMS_ID);
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            while (rs.next()){
                teamsId.add(rs.getInt("team_id"));
            }
            teams = addTeams(teamsId);
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            close(ps);
            close(rs);
        }
        return teams;
    }

    private List<Team> addTeams(List<Integer> teamsId) {
        List<Team> res = new ArrayList<>();
        List<Team> allTeams = findAllTeams();
        for (Integer id : teamsId){
            for (Team team : allTeams){
                if (team.getId() == id){
                    res.add(team);
                }
            }
        }
        return res;
    }

    private void close(PreparedStatement ps) {
        if (ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                LOGGER.severe(e.getMessage());
            }
        }
    }

    private void close(ResultSet rs) {
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.severe(e.getMessage());
            }
        }
    }

    public User getUser(String login){
        PreparedStatement ps = null;
        User user = null;
        ResultSet rs = null;
        try(Connection connection = getConnection(URL)) {
            ps = connection.prepareStatement(DBConstants.GET_USER);
            ps.setString(1, login);
            rs = ps.executeQuery();
            while (rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("login"));
            }
        } catch (SQLException e) {
            LOGGER.warning("No such user " + e.getMessage());
        } finally {
            close(ps);
            close(rs);
        }
        return user;
    }

    public Team getTeam(String name){
        PreparedStatement ps = null;
        Team team = null;
        ResultSet rs = null;
        try (Connection connection = getConnection(URL)){
            ps = connection.prepareStatement(DBConstants.GET_TEAM);
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()){
                team = new Team();
                team.setId(rs.getInt("id"));
                team.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            LOGGER.warning("No such user " + e.getMessage());
        } finally {
            close(ps);
            close(rs);
        }
        return team;
    }

    public void deleteTeam(Team team){
        PreparedStatement ps = null;
        try (Connection connection = getConnection(URL)){
            ps = connection.prepareStatement(DBConstants.DELETE_TEAM_BY_NAME);
            ps.setString(1, team.getTeamName());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning("No such team " + e.getMessage());
        } finally {
            close(ps);
        }
    }

    public void updateTeam(Team team){
        PreparedStatement ps = null;
        try(Connection connection = getConnection(URL)) {
            ps = connection.prepareStatement(DBConstants.UPDATE_TEAM);
            ps.setString(1, team.getTeamName());
            ps.setInt(2, team.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning("Can`t update team " + e.getMessage());
        } finally {
            close(ps);
        }
    }
}
