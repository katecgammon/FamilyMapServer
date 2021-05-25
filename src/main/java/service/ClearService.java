package service;


import dao.DataAccessException;
import dao.Database;
import result.ClearResult;

import java.sql.Connection;

public class ClearService {
    /**
     *
     * Deletes all the data in the database. Clears all tables
     *
     * @return success message or an error message.
     */
    public static ClearResult clear() throws DataAccessException {
        Database db = new Database();
        Connection conn = db.getConnection();

        ClearResult result = new ClearResult();

        try {
            //db.openConnection();
            db.clearTables();
            db.closeConnection(true);
            result.setSuccess(true);
            result.setMessage("Clear succeeded.");
        }
        catch (DataAccessException ex) {
            result.setSuccess(false);
            result.setMessage("Error: Could not clear tables");
            db.closeConnection(false);
        }
        return result;
    }
}
