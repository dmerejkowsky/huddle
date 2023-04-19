package info.dmerej.huddle;

import java.util.List;

public interface Storage {

    void reset();

    Account createAccount(AccountCreationRequest request);

    Account getAccountById(int id);

    Account getAccountByUserName(String username);

    Huddle scheduleHuddle(HuddleScheduleRequest request);

    Huddle getHuddleByDate(String date);

    Huddle getHuddleById(int id);

    void registerParticipant(int accountId, int huddleId);

    List<Integer> getParticipantsForHuddle(int huddleId);

    List<Account> getAllAccounts();
}
