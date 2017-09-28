package br.cefetmg.chat.implementation.dao;

import br.cefetmg.chat.interfaces.dao.IUserDAO;
import br.cefetmg.chat.domain.User;
import br.cefetmg.chat.exception.PersistenceException;
import br.cefetmg.chat.util.db.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements IUserDAO{

    @Override
    public synchronized User insertUser(User u) throws PersistenceException {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            String sql = "INSERT INTO users(ipUser, nameUser) VALUES(?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, u.getIpUser());
            pstmt.setString(2, u.getNameUser());
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
            return u;
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public synchronized User getUserById(Long id) throws PersistenceException {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();

            String sql = "SELECT ipUser, nameUser FROM Users WHERE ipUser = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            User us = new User();
            if (rs.next()) {
                us.setIpUser(rs.getLong("ipUser"));
                us.setNameUser(rs.getString("nameUser"));
            }
            rs.close();
            pstmt.close();
            connection.close();
            return us;
        } catch (ClassNotFoundException | SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public synchronized User deleteUserById(Long id) throws PersistenceException {
        try {
            User us = this.getUserById(id);
            Connection connection = ConnectionManager.getInstance().getConnection();
            String sql = "DELETE FROM Users WHERE ipUser = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
            return us;
        } catch (PersistenceException | ClassNotFoundException | SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public synchronized User updateUserById(Long id, User u) throws PersistenceException {
        try{
            Connection connection = ConnectionManager.getInstance().getConnection();
            String sql = "UPDATE Users SET ipUser = ?, nameUser = ? WHERE ipUser = ? ";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, u.getIpUser());
            pstmt.setString(2, u.getNameUser());
            pstmt.setLong(3, u.getIpUser());
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
            return u;
        } catch (ClassNotFoundException | SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }
    
}
