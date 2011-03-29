package server.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Klasse som tilkobler databasen og sender spørringer.
 * Klassen inneholder metoder både for å hente og for å
 * skrive informasjon til databasen.
 * 
 * @version 1.0
 */
public abstract class Database {
	private static Connection con;
	public static final String DATABASE = "db"; // Databasenavn
	
	/**
	 * Return type som brukes for modify metoden
	 */
	public static enum ReturnType {
		ID, ROWS
	}

	/**
	 * Metode for å sende SELECT spørringer til databasen
	 * Bruker IKKE PreparedStatements
	 * 
	 * @param sql Spørring som skal sendes til databasen
	 * @return Resultat
	 */
	public static List<HashMap<String, String>> retrieve(String sql) {
		return retrieve(sql, new String[0]);
	}
	
	/**
	 * Metode for å sende SELECT spørringer til databasen
	 * Bruker PreparedStatements
	 * 
	 * @param sql Spørring som skal sendes til databasen
	 * @param params String array med parametere til spørringen
	 * @return Resultat
	 */
	public static List<HashMap<String, String>> retrieve(String sql, String[] params) {
		List<HashMap<String, String>> rows = 
			new ArrayList<HashMap<String, String>>();
		
		connect();
		
		try {
			PreparedStatement sprep = con.prepareStatement(sql);
			
			for (int i = 0; i < params.length; i++) {
				sprep.setObject(i + 1, params[i]);
			}
			
			ResultSet rs = sprep.executeQuery();
			
			while (rs.next()) {	
				HashMap<String, String> hm = new HashMap<String, String>();
				ResultSetMetaData rsmt = rs.getMetaData();
				
				for (int i = 1; i < rsmt.getColumnCount() + 1; i++) {
					hm.put(rsmt.getColumnName(i).toLowerCase(), rs.getString(i));
				}
				
				rows.add(hm);
			}
		} catch (SQLException e) {
			// TODO: When release: Remove print
			System.out.println(e.getMessage());
			if ((e.getErrorCode() == 0) && e.getSQLState().equals("08S01")) {
				
				// Prøver å reconnecte hvis Connection er dropped
				connect(true);
				return retrieve(sql, params);
			}
		}
		
		return rows;
	}
	
	/**
	 * Metode for å sende oppdaterings-spørringer til databasen
	 * Bruker Prepared Statements
	 * @param sql Spørringen som skal sendes til databasen
	 * @param params String array med parametere til spørringen
	 * @return Returnerer id'en til det oppdaterte feltet hvis et ble oppdatert
	 */
	public int modify(String sql, String[] params) {
		return modify(sql, params, ReturnType.ID);
	}
	
	/**
	 * Metode for å sende oppdaterings-spørringer til databasen
	 * Bruker Prepared Statements
	 * 
	 * @param sql Spørring som skal sendes til databasen
	 * @param params String array med parametere til spørringen
	 * @param type Hva metoden skal returnere
	 * @return Den gitte typen
	 */
	public static int modify(String sql, String[] params, ReturnType type) {
		connect();
		
		try {
			PreparedStatement sprep = 
				con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			for (int i = 0; i < params.length; i++) {
				sprep.setObject(i + 1, params[i]);
			}
			
			int rows = sprep.executeUpdate();
			int id = 0;
			
			if (type == ReturnType.ROWS) {
				return rows;
			} else if (type == ReturnType.ID) {
				ResultSet rs = sprep.getGeneratedKeys();
				
				if (rs.next()) {
					id = rs.getInt(1);
				}
				
				return id;
			}
		} catch (SQLException e) {
			// TODO: When release: Remove print
			System.out.println(e.getMessage());
			if ((e.getErrorCode() == 0) && e.getSQLState().equals("08S01")) {
				
				// Prøver å reconnecte hvis Connection er dropped
				connect(true);
				return modify(sql, params, type);
			}
		}
		
		return 0;
	}
	
	/**
	 * Metode som oppretter en tilkobling til databasen.
	 * Hvis tilkoblingen er allerede avbrutt vil den opprette på nytt
	 * @param force Brukes når Connection skal reconnectes
	 */
	private static void connect(Boolean force) {
		try {
			if ((con == null) || con.isClosed() || force) {
				System.out.println("New connection established");
				Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
				con = DriverManager.getConnection("jdbc:derby:" + DATABASE);
			}
		} catch (Exception e) {
			// TODO: Handle failed connect
			System.out.println(e.getMessage());
		}
	}
	
	private static void connect() {
		connect(false);
	}
	
	/**
	 * @return toString() av Connection objektet til klassen
	 */
	public String toString() {
		return con.toString();
	}
}