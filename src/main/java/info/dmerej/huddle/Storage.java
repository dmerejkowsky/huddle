package info.dmerej.huddle;

import java.util.List;

public interface Storage {

    void reset();

    Account createAccount(Identity identity);

    Account getAccountById(int id);

    Account getAccountByUserName(String username);

    Huddle scheduleHuddle(HuddleAnnounce announce);

    Huddle getHuddleByDate(String date);

    Huddle getHuddleById(int id);

    Participant registerParticipant(Account account, Huddle huddle);

    List<Participant> getParticipantsForHuddle(Huddle huddle);

    List<Account> getAllAccounts();

    List<Huddle> getAllHuddles();

    List<Huddle> attendedBy(Account account);
}
