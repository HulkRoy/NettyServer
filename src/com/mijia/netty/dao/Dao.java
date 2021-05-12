package com.mijia.netty.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Dao
{
    private static ComboPooledDataSource ds;
    
    static {
        Dao.ds = new ComboPooledDataSource();
    }
    
    public static boolean uploadData(final int msgid, final String action, final String message) {
        final String sql = "insert into netty_data(msgid, action, message, savetime) values(?, ?, ?,now())";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean res = false;
        try {
            conn = Dao.ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, msgid);
            pstmt.setString(2, action);
            pstmt.setString(3, message);
            int flag = pstmt.executeUpdate();
            if (flag == 1) {
            	res = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return res;
    }
    

    public static boolean heartbeatInsert(final String imei, final String heartbeat) {
        final String sql = "insert into netty_heartbeat(imei, heartbeat, savetime) values(?,?,now())";
        Connection conn = null;
        PreparedStatement pstmt = null;
        int flag = 0;
        try {
            conn = Dao.ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, imei);
            pstmt.setString(2, heartbeat);
            flag = pstmt.executeUpdate();            
        }
        catch (SQLException e) {
            e.printStackTrace();
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException e2) {
                e2.printStackTrace();
            }
            return flag > 0;
        }
        finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return flag > 0;
    }
    
    public static boolean initializeChannels(final String imei) {
        final String sql = "insert IGNORE into netty_channel(imei, channel) values(?, ?);";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean res = false;
        try {
            conn = Dao.ds.getConnection();
            for (int i = 0; i < 16; i++) {
            	pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, imei);
                pstmt.setInt(2, i);
                pstmt.executeUpdate();
            	pstmt.close();
            }
            res = true;
        }
        catch (SQLException e) {
        	res = false;
            e.printStackTrace();
        }
        finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return res;
    }
    
    public static int getLatestMsgId(final String imei) {
        int result = 0;
        final String sql = "select msgid from netty_imei where imei=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Dao.ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, imei);
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	result = rs.getInt("msgid");
            }
        }
        catch (Exception e) {
            result = -1;
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
    public static String updateMsgId(int msgid, String imei) {
        String result = "";
        String sql = "INSERT INTO netty_imei (imei, msgid, heartbeat) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE msgid=?, heartbeat=?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Dao.ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, imei);
            pstmt.setInt(2, msgid);
            pstmt.setString(3, "online");
            pstmt.setInt(4, msgid);
            pstmt.setString(5, "online");
            int flag = pstmt.executeUpdate();
			if (flag != 0) {
				result = "suc";
			}
        }
        catch (Exception e) {
        	result = "err";
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
	}
    
    public static String updateGoodsByChannelIndex(String imei, int channel, int goods, String saleid, int alarmcode) {
        String result = "";
        String sql = "INSERT INTO netty_channel (imei, channel, goods, saleid, alarmcode) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE goods=?, saleid=?, alarmcode=?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Dao.ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, imei);
            pstmt.setInt(2, channel);
            pstmt.setInt(3, goods);
            pstmt.setString(4, saleid);
            pstmt.setInt(5, alarmcode);
            pstmt.setInt(6, goods);
            pstmt.setString(7, saleid);
            pstmt.setInt(8, alarmcode);
            int flag = pstmt.executeUpdate();
			if (flag != 0) {
				result = "suc";
			}
        }
        catch (Exception e) {
        	result = "err";
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
	}
    
    public static String updateSkipGoodsByChannelIndex(String imei, int channel, String saleid, int alarmcode) {
        String result = "";
        String sql = "INSERT INTO netty_channel (imei, channel, saleid, alarmcode) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE saleid=?, alarmcode=?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Dao.ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, imei);
            pstmt.setInt(2, channel);
            pstmt.setString(3, saleid);
            pstmt.setInt(4, alarmcode);
            pstmt.setString(5, saleid);
            pstmt.setInt(6, alarmcode);
            int flag = pstmt.executeUpdate();
			if (flag != 0) {
				result = "suc";
			}
        }
        catch (Exception e) {
        	result = "err";
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
	}
    
    public static String updateAllGoods(String imei, int goods) {
        String result = "";
        String sql = "UPDATE netty_channel set goods=? where imei=?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Dao.ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, goods);
            pstmt.setString(2, imei);
            int flag = pstmt.executeUpdate();
			if (flag != 0) {
				result = "suc";
			}
        }
        catch (Exception e) {
        	result = "err";
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
	}
    
    public static String updateTemperature(String imei, int msgid, int temperature) {
        String result = "";
        String sql = "UPDATE netty_imei set msgid=?, temperature=?, heartbeat=? where imei=?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Dao.ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, msgid);
            pstmt.setInt(2, temperature);
            pstmt.setString(3, "online");
            pstmt.setString(4, imei);
            int flag = pstmt.executeUpdate();
			if (flag != 0) {
				result = "suc";
			}
        }
        catch (Exception e) {
        	result = "err";
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
	}
    
    public static String updateCSQ(String imei, int msgid, int csqlevel) {
        String result = "";
        String sql = "UPDATE netty_imei set msgid=?, csqlevel=?, heartbeat=? where imei=?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Dao.ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, msgid);
            pstmt.setInt(2, csqlevel);
            pstmt.setString(3, "online");
            pstmt.setString(4, imei);
            int flag = pstmt.executeUpdate();
			if (flag != 0) {
				result = "suc";
			}
        }
        catch (Exception e) {
        	result = "err";
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
	}
    
    public static String updateLocation(String imei, int msgid, String location) {
        String result = "";
        String sql = "UPDATE netty_imei set msgid=?, location=?, heartbeat=? where imei=?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Dao.ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, msgid);
            pstmt.setString(2, location);
            pstmt.setString(3, "online");
            pstmt.setString(4, imei);
            int flag = pstmt.executeUpdate();
			if (flag != 0) {
				result = "suc";
			}
        }
        catch (Exception e) {
        	result = "err";
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
	}
    
    public static String updateOfflineImei(String imei) {
        String result = "";
        String sql = "UPDATE netty_imei set heartbeat=? where imei=?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Dao.ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "offline");
            pstmt.setString(2, imei);
            int flag = pstmt.executeUpdate();
			if (flag != 0) {
				result = "suc";
			}
        }
        catch (Exception e) {
        	result = "err";
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
	}

}
