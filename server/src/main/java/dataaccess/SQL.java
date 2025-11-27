package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.Data;
import model.UserData;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public abstract class SQL
{
    public int executeUpdate(String statement, Object... params) throws ResponseException, DataAccessException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS))
            {
                for (int i = 0; i < params.length; i++)
                {
                    Object param = params[i];
                    if (param instanceof String p)
                    {
                        ps.setString(i + 1, p);
                    }
                    else if (param instanceof Integer p)
                    {
                        ps.setInt(i + 1, p);
                    }
                    else if (param == null)
                    {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next())
                {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e)
        {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    public void configureDatabase(String[] createStatements) throws ResponseException, DataAccessException
    {
        DatabaseManager.createDatabase();
        try(Connection conn = DatabaseManager.getConnection())
        {
            for(String statement : createStatements)
            {
                try(var preparedStatement = conn.prepareStatement(statement))
                {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch(SQLException ex)
        {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    public Data getClassInfo(String username, String statement) throws ResponseException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return readClass(rs);
                    }
                }
            }
        } catch (Exception e)
        {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public abstract Data readClass(ResultSet rs) throws SQLException;


    public void clearData() throws DataAccessException
    {
        try(Connection conn = DatabaseManager.getConnection())
        {
            String sql = "SHOW TABLES";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            Statement truncateStmt = conn.createStatement();
            truncateStmt.execute("SET FOREIGN_KEY_CHECKS=0");

            while (rs.next())
            {
                String table = rs.getString(1);
                truncateStmt.executeUpdate("TRUNCATE TABLE " + table);
            }

            truncateStmt.execute("SET FOREIGN_KEY_CHECKS=1");
        }
        catch(SQLException e)
        {
            throw new DataAccessException("SQL not functional");
        }

    }

}
