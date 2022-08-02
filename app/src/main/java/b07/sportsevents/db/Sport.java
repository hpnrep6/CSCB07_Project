package b07.sportsevents.db;

public class Sport extends DBTable<Sport> {
    public String name;
    protected long ID;

    public static String getTableName() {
        return "Sports";
    }

    public Sport getInstance() {
        return new Sport();
    }
}
