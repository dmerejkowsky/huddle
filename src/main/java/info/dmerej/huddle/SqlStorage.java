package info.dmerej.huddle;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
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


    @Override
    public void reset() {
        var flyway = getFlyway();
        flyway.clean();
        flyway.migrate();
    }

    private Flyway getFlyway() {
        return Flyway.configure()
            .locations("filesystem:src/main/resources/db/migration/")
            .dataSource(this.url, "", "")
            .load();
    }


    @Override
    public Account createAccount(Identity identity) {
        try {
            var sql = """
                INSERT INTO accounts(username, email)
                VALUES(?, ?)
                """;
            var statement = connection.prepareStatement(sql);
            statement.setString(1, identity.username());
            statement.setString(2, identity.email());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("When inserting account: " + e);
        }
        return getAccountByUserName(identity.username());
    }

    @Override
    public Account getAccountById(int id) {
        try {
            var sql = """
                SELECT id, username, email FROM accounts WHERE id = ?
                """;
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Account(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            } else {
                throw new NoSuchAccount(String.format("No account found for id '%d'", id));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("When looking for account with id %d : %s", id, e));
        }
    }

    @Override
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
                return new Account(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            } else {
                throw new NoSuchAccount(String.format("No account found for username '%s'", username));
            }

        } catch (SQLException e) {
            throw new RuntimeException(String.format("When looking for account named %s : %s", username, e));
        }
    }

    @Override
    public Huddle scheduleHuddle(HuddleAnnounce announce) {
        try {
            var sql = """
                INSERT INTO huddles(date, title)
                VALUES(?, ?)
                """;
            var statement = connection.prepareStatement(sql);
            statement.setString(1, announce.date());
            statement.setString(2, announce.title());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("When inserting huddle: " + e);
        }
        return getHuddleByDate(announce.date());
    }

    @Override
    public Huddle getHuddleByDate(String date) {
        try {
            var sql = """
                SELECT id, date, title FROM huddles WHERE date = ?
                """;
            var statement = connection.prepareStatement(sql);
            statement.setString(1, date);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Huddle(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            } else {
                throw new NoSuchHuddle(String.format("No huddle found for date '%s'", date));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("When looking for huddle for date %s : %s", date, e));
        }
    }

    @Override
    public Huddle getHuddleById(int id) {
        try {
            var sql = """
                SELECT id, date, title FROM huddles WHERE id = ?
                """;
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Huddle(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            } else {
                throw new NoSuchHuddle(String.format("No huddle found with id '%d'", id));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("When looking for huddle with id %d : %s", id, e));
        }
    }

    @Override
    public void registerParticipant(Account account, Huddle huddle) {
        try {
            var sql = """
                INSERT INTO participants(account_id, huddle_id)
                VALUES(?, ?)
                """;
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, account.id());
            statement.setInt(2, huddle.id());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("When inserting participants: " + e);
        }
    }

    @Override
    public List<Participant> getParticipantsForHuddle(Huddle huddle) {
        List<Integer> ids = new ArrayList<>();

        try {
            var sql = """
                SELECT account_id FROM participants WHERE huddle_id = ?
                """;
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, huddle.id());
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getInt(1);
                ids.add(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("When looking for huddle with id %d : %s", huddle.id(), e));
        }

        return ids.stream().map(this::getAccountById).map(account -> new Participant(account, huddle)).toList();
    }

    @Override
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
