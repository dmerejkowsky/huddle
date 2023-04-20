package info.dmerej.huddle;

import java.util.List;

public class Meeting {
    private final Storage storage;
    private final Huddle huddle;

    public Meeting(Storage storage, Huddle huddle) {
        this.storage = storage;
        this.huddle = huddle;
    }

    public List<Participant> firstTimeParticipants() {
        var participants = storage.getParticipantsForHuddle(huddle);
        return participants;
    }
}
