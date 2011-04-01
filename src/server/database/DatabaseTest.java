package server.database;

import java.util.HashMap;
import java.util.List;

public class DatabaseTest {
	public static void main(String[] args) {
		String sql;
		/*
		sql = "INSERT INTO Employees (username, name, password) VALUES (?, ?, ?)";
		String[] params = {"arne", "Arne Bjarne", "arne123pass"};
		System.out.println("Modified: " + Database.modify(sql, params, Database.ReturnType.ROWS));
		*/
		sql = "SELECT * FROM Employees";
		List<HashMap<String, String>> res = Database.retrieve(sql);
		System.out.println(res.size());
		for (HashMap<String, String> hm : res) {
			System.out.println(hm.get("username"));
			System.out.println(hm.get("name"));
			System.out.println(hm.get("password"));
		}
		res = Database.retrieve(sql);
		res = Database.retrieve(sql);
		res = Database.retrieve(sql);
	}
}