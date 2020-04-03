package com.compulynx.compas.dal.operations;
import java.sql.*;

public class DBOperations {
	public static void DisposeSql(Connection connection,
			PreparedStatement statement, ResultSet set) {
		try {
			if (set != null) {
				set.close();
				set = null;
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void DisposeSql(Connection connection,
								  Statement statement, ResultSet set) {
		try {
			if (set != null) {
				set.close();
				set = null;
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void DisposeSql(Connection connection,
			CallableStatement statement, ResultSet set) {
		try {
			if (set != null) {
				set.close();
				set = null;
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void DisposeSql(Connection connection,
			CallableStatement statement) {
		try {
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void DisposeSql(Connection connection,
			PreparedStatement statement) {
		try {
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void DisposeSql(PreparedStatement statement) {
		try {
			if (statement != null) {
				statement.close();
				statement = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void DisposeSql(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void DisposeSql(Connection connection, ResultSet set) {
		try {
			if (set != null) {
				set.close();
				set = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void DisposeSql(PreparedStatement statement, ResultSet set) {
		try {
			if (set != null) {
				set.close();
				set = null;
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void DisposeSql(ResultSet set) {
		try {
			if (set != null) {
				set.close();
				set = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
