package info.dmerej.huddle;

import java.util.List;

public class Calendar {
    private final Storage storage;

    public Calendar(Storage storage) {
        this.storage = storage;
    }

    public void scheduleHuddle(HuddleAnnounce huddle) {

    }

    public List<Huddle> pastHuddles() {
        var allHuddles = storage.getAllHuddles();
        return allHuddles.stream().filter(h -> h.date().compareTo("2023-04-20") < 0).toList();
    }
}
