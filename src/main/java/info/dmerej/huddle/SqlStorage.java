package info.dmerej.huddle;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage {
    public final Connection connection;
    public final String url;

    public SqlStorage(String url) {
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException("When connecting: " + e);
        }
        this.url = url;
    }

    public void migrate() {
        var flyway = getFlyway();
        flyway.migrate();
    }


    public void reset() {
        var flyway = getFlyway();
        flyway.clean();
        flyway.migrate();
    }

    private Flyway getFlyway() {
        Flyway flyway = Flyway.configure().locations("filesystem:db/migration/").dataSource(this.url, "", "").load();
        return flyway;
    }


    public Account createAccount(AccountCreationRequest request) {
        try {
            var sql = """
                INSERT INTO accounts(username, email)
                VALUES(?, ?)
                """;
            var statement = connection.prepareStatement(sql);
            statement.setString(1, request.username());
            statement.setString(2, request.email());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("When inserting account: " + e);
        }
        return getAccountByUserName(request.username());
    }

    public Account getAccountById(int id) {
        try {
            var sql = """
                SELECT id, username, email FROM accounts WHERE id = ?
                """;
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var account = new Account(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                return account;
            } else {
                throw new NoSuchAccount(String.format("No account found for id '%d'", id));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("When looking for account with id %d : %s", id, e));
        }
    }

    public Account getAccountByUserName(String username) {
        ResultSet resultSet;
        try {
            var sql = """
                SELECT id, username, email FROM accounts WHERE username = ?
                """;
            var statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var account = new Account(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                return account;
            } else {
                throw new NoSuchAccount(String.format("No account found for username '%s'", username));
            }

        } catch (SQLException e) {
            throw new RuntimeException(String.format("When looking for account named %s : %s", username, e));
        }
    }

    public Huddle scheduleHuddle(HuddleScheduleRequest request) {
        try {
            var sql = """
                INSERT INTO huddles(date, title)
                VALUES(?, ?)
                """;
            var statement = connection.prepareStatement(sql);
            statement.setString(1, request.date());
            statement.setString(2, request.title());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("When inserting huddle: " + e);
        }
        return getHuddleByDate(request.date());
    }

    public Huddle getHuddleByDate(String date) {
        try {
            var sql = """
                SELECT id, date, title FROM huddles WHERE date = ?
                """;
            var statement = connection.prepareStatement(sql);
            statement.setString(1, date);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var huddle = new Huddle(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                return huddle;
            } else {
                throw new NoSuchHuddle(String.format("No huddle found for date '%s'", date));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("When looking for huddle for date %s : %s", date, e));
        }
    }

    public Huddle getHuddleById(int id) {
        try {
            var sql = """
                SELECT id, date, title FROM huddles WHERE id = ?
                """;
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var huddle = new Huddle(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                return huddle;
            } else {
                throw new NoSuchHuddle(String.format("No huddle found with id '%d'", id));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("When looking for huddle with id %d : %s", id, e));
        }
    }

    public void registerParticipant(int accountId, int huddleId) {
        try {
            var sql = """
                INSERT INTO participants(account_id, huddle_id)
                VALUES(?, ?)
                """;
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, accountId);
            statement.setInt(2, huddleId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("When inserting participants: " + e);
        }
    }

    public List<Integer> getParticipantsForHuddle(int huddleId) {
        List<Integer> res = new ArrayList<>();

        try {
            var sql = """
                SELECT account_id FROM participants WHERE huddle_id = ?
                """;
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, huddleId);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getInt(1);
                res.add(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("When looking for huddle with id %d : %s", huddleId, e));
        }

        return res;
    }

    public List<Account> getAllAccounts() {
        var res = new ArrayList<Account>();
        try {
            var sql = """
                SELECT id, username, email FROM accounts
                """;
            var statement = connection.prepareStatement(sql);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var account = new Account(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("email"));
                res.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("When fetching account: %s", e));
        }
        return res;
    }
}
