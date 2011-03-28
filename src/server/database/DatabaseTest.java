package server.database;

import java.util.HashMap;
import java.util.List;

public class DatabaseTest {
	public static void main(String[] args) {
		String sql;
		
		/*
		sql = "INSERT INTO Person (name, tlf, email) VALUES (?, ?, ?)";
		String[] params = {"Arne", "239402", "arne@email.com"};
		System.out.println("Modified: " + db.modify(sql, params, Database.ReturnType.ROWS));
		*/
		sql = "SELECT * FROM person";
		List<HashMap<String, String>> res = Database.retrieve(sql);
		System.out.println(res.size());
		for (HashMap<String, String> hm : res) {
			System.out.println(hm.get("name"));
			System.out.println(hm.get("tlf"));
			System.out.println(hm.get("email"));
		}
		res = Database.retrieve(sql);
		res = Database.retrieve(sql);
		res = Database.retrieve(sql);
	}
}