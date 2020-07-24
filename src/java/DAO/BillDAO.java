/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.*;

/**
 *
 * @author Group 1
 */
public class BillDAO {

    public Connection conn;

    public BillDAO() {
        DBConnection db = new DBConnection();
        this.conn = db.getDBConnection();
    }

    /**
     * Get All Bill
     *
     * @return
     * @throws SQLException
     */
    public ResultSet getAll() throws SQLException {
        String sql = "SELECT * FROM `bill`";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = null;

        try {
            rs = ps.executeQuery();
        } catch (Exception e) {
        };

        return rs;
    }

    /**
     * Get bill by given bill id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Bill getBillById(int id) throws SQLException {
        String sql = "SELECT * FROM `bill` WHERE `bId` = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = null;
        Bill bill = null;
        try {
            rs = ps.executeQuery();
            if (rs.next()) {
                bill = new Bill(id, rs.getInt("cusId"), rs.getInt("sId"), rs.getDate("dateBuy"), rs.getLong("total"), rs.getString("name"), rs.getString("phone"), rs.getInt("status"));
            }
        } catch (Exception e) {
        }

        return bill;
    }

    public ResultSet getIdBillByCustomer(int cusId) throws SQLException {
        String sql = "SELECT  * FROM `bill` WHERE `cusId` = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, cusId);
        ResultSet rs = ps.executeQuery();

        return rs;

    }

    /**
     * Create new bill
     *
     * @param cusId
     * @param total
     * @return
     * @throws SQLException
     */
    public boolean createBill(int cusId, long total, String phone, String name) throws SQLException {
        String sql = "INSERT INTO `bill`(`cusId`, `total`,`name`, `phone`) values (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, cusId);
        ps.setInt(2, (int) total);
        ps.setString(3, name);
        ps.setString(4, phone);

        try {
            int insert = ps.executeUpdate();
            if (insert == 1) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
        };

        return false;
    }

    /**
     * Update bill by given values
     *
     * @param bId
     * @param sId
     * @param total
     * @return
     * @throws SQLException
     */
    public boolean updateBill(int bId, int sId, long total, int status, String phone, String name) throws SQLException {
        String sql = "UPDATE `bill` SET `sId` = ?, `total` = ?, `status` = ?, `phone` = ?, `name` = ? WHERE `bId` = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, sId);
        ps.setLong(2, total);
        ps.setInt(3, status);
        ps.setString(4, phone);
        ps.setString(5, name);
        ps.setInt(6, bId);

        int rs = ps.executeUpdate();
        return rs > 0 ? true : false;
    }

    public boolean updateBillStatus(int bId, int status) throws SQLException {
        String sql = "UPDATE `bill` SET `status` = ? WHERE `bId` = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, status);
        ps.setInt(2, bId);

        int rs = ps.executeUpdate();
        return rs > 0 ? true : false;
    }

    /**
     * Get max bill id
     *
     * @return
     */
    public int maxBill() {
        String sql = "Select max(bId) as bId from bill";

        PreparedStatement st;
        try {
            st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("bId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }
}
