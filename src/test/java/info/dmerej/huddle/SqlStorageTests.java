package info.dmerej.huddle;

public class SqlStorageTests extends AbstractStorageTest {
    public static SqlStorage inMemory() {
        return new SqlStorage("jdbc:h2:mem:huddle");
    }

    @Override
    Storage getStorage() {
        return inMemory();
    }


}
