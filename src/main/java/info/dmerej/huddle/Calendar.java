package info.dmerej.huddle;

import java.util.List;

public class Calendar {
    private final Storage storage;

    public Calendar(Storage storage) {
        this.storage = storage;
    }

    public void scheduleHuddle(HuddleAnnounce announce) {
        storage.scheduleHuddle(announce);
    }

    public List<Huddle> pastHuddles() {
        var allHuddles = storage.getAllHuddles();
        return allHuddles.stream().filter(h -> h.date().compareTo("2022-01-01") < 0).toList();
    }

    public List<Participant> firstTimeParticipants(Huddle huddle) {
        var allParticipants = storage.getParticipantsForHuddle(huddle);
        return allParticipants.stream().filter(p -> storage.attendedBy(p.account()).size() <= 1).toList();
    }
}
