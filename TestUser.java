import java.sql.*;

public class TestUser {
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	final static String url = "jdbc:postgresql://127.0.0.1:5432/simplechatdb";
	final static String user = "simplechat";
	final static String passwd = "simplechat";

	public Connection connect() throws SQLException {
		return DriverManager.getConnection(url, user, passwd);
	}

	public void selectSCUser() {
		String sql = "select * from sc_user";


		try (Connection con = DriverManager.getConnection(url,user,passwd);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql)) {
			
			System.out.println("--------------------------------------------");

			while (rs.next()) {
				int id = rs.getInt("id");
				String tmp1 = rs.getString("userid");
				String tmp2 = rs.getString("passwd");
				String email = rs.getString("email");
				System.out.println(id + "|" + tmp1 + " " + tmp2 + " " + email);
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public int findSCUser(String userid, String password) {
		String sql = "select count(*) from sc_user where userid = ? and passwd = ?";
		int count = 0;

		try (Connection con = DriverManager.getConnection(url, user, passwd);
			PreparedStatement stmt = con.prepareStatement(sql)) {
			
			stmt.setString(1, userid);
			stmt.setString(2, password);

			ResultSet rs = stmt.executeQuery();
			rs.next();

			count = rs.getInt(1);
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return count;
	}

	public int insertSCUser(String userid, String password, String email) {
		String sql = "insert into sc_user(userid, passwd, email) values(?, ?, ?)";

		int id = 0;

		try (Connection con = DriverManager.getConnection(url, user, passwd);
			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, userid);
			stmt.setString(2, password);
			stmt.setString(3, email);

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet rs = stmt.getGeneratedKeys()) {
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return id;
	}

	public int updateSCUser(String userid, String password, String email) {
		String sql = "update sc_user "
			+ " set email = ? "
			+ " where userid = ? and passwd = ? ";

		int affectedRows = 0;

		try (Connection con = DriverManager.getConnection(url, user, passwd);
			PreparedStatement stmt = con.prepareStatement(sql)) {
			
			stmt.setString(1, email);
			stmt.setString(2, userid);
			stmt.setString(3, password);

			affectedRows = stmt.executeUpdate();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return affectedRows;
	}

	public int deleteSCUser(String userid, String password) {
		
		String sql = "delete from sc_user "
			+ " where userid = ? and passwd = ?";

		int affectedRows = 0;

		try (Connection con = DriverManager.getConnection(url, user, passwd);
			PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setString(1, userid);
			stmt.setString(2, password);

			affectedRows = stmt.executeUpdate();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return affectedRows;
	}

	public static void main(String[] args) {

		String url = "jdbc:postgresql://127.0.0.1:5432/simplechatdb";
		String user = "simplechat";
		String passwd = "simplechat";

		String sql = "select * from sc_user";

		try (Connection con = DriverManager.getConnection(url, user, passwd);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql)) {
			
			System.out.println("------------------------------------------");

			while (rs.next()) {
				int id = rs.getInt("id");
				String tmp1 = rs.getString("userid");
				String tmp2 = rs.getString("passwd");
				String email = rs.getString("email");
				System.out.println(id + "|" + tmp1 + " " + tmp2 + " " + email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int rows = 0;

		TestUser tu = new TestUser();
		tu.selectSCUser();

		int count = tu.findSCUser("sonia", "sonia1234");
		System.out.println("sonia : " + count);

		int id = tu.insertSCUser("hyk", "hyk1234", "hyk@gmail.com");
		System.out.println("hyk : " + id);

		rows = tu.updateSCUser("hyk", "hyk1234", "hyk@naver.com");
		System.out.println("[update]affected rows : " + rows);

		rows = tu.deleteSCUser("hyk", "hyk1234");
		System.out.println("[delete]affected rows : " + rows);

		tu.selectSCUser();
	}
}

