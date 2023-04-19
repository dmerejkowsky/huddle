package info.dmerej.huddle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class FakeStorage implements Storage {
    private final HashMap<Integer, Account> accounts;
    private final HashMap<Integer, Huddle> huddles;
    private final ArrayList<int[]> participants;

    public FakeStorage() {
        accounts = new HashMap<>();
        huddles = new HashMap<>();
        participants = new ArrayList<>();
    }

    @Override
    public void reset() {
        accounts.clear();
        huddles.clear();
        participants.clear();
    }

    @Override
    public Account createAccount(AccountCreationRequest request) {
        var maxId = accounts.keySet().stream().max(Comparator.naturalOrder()).orElse(0);
        var id = maxId + 1;
        var account = new Account(id, request.username(), request.email());
        accounts.put(id, account);
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accounts.values().stream().toList();
    }

    @Override
    public Account getAccountById(int id) {
        var res = accounts.get(id);
        if (res == null) {
            throw new NoSuchAccount(String.format("No account found for id '%d'", id));
        }
        return res;
    }

    @Override
    public Account getAccountByUserName(String username) {
        var res = accounts.values().stream().filter(account -> account.username().equals(username)).findFirst();
        if (res.isEmpty()) {
            throw new NoSuchAccount(String.format("No account found for username '%s'", username));
        }
        return res.get();
    }

    @Override
    public Huddle scheduleHuddle(HuddleScheduleRequest request) {
        var maxId = huddles.keySet().stream().max(Comparator.naturalOrder()).orElse(0);
        var id = maxId + 1;
        var huddle = new Huddle(id, request.date(), request.title());
        huddles.put(id, huddle);
        return huddle;
    }

    @Override
    public Huddle getHuddleByDate(String date) {
        var res = huddles.values().stream().filter(huddle -> huddle.date().equals(date)).findFirst();
        if (res.isEmpty()) {
            throw new NoSuchHuddle(String.format("No huddle found for date '%s'", date));
        }
        return res.get();
    }

    @Override
    public Huddle getHuddleById(int id) {
        var res = huddles.get(id);
        if (res == null) {
            throw new NoSuchHuddle(String.format("No huddle found for id '%d'", id));
        }
        return res;
    }

    @Override
    public void registerParticipant(int accountId, int huddleId) {
        var tuple = new int[]{accountId, huddleId};
        participants.add(tuple);
    }

    @Override
    public List<Integer> getParticipantsForHuddle(int huddleId) {
        return participants.stream().filter(t -> t[1] == huddleId).map(t -> t[0]).toList();
    }
}
