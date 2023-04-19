package info.dmerej.huddle;

public class FakeStorageTests extends AbstractStorageTest {
    @Override
    Storage getStorage() {
        return new FakeStorage();
    }
}
