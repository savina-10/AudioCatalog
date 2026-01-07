public class Album extends AudioItem {
    private final int tracksCount;

    public Album(int id, String title, String author, String genre,
                 String duration, String category, int year,
                 int tracksCount) {
        super(id, title, author, genre, duration, category, year);
        this.tracksCount = tracksCount;
    }


    @Override
    public String getTypeCode() {
        return "ALBUM";
    }

    @Override
    public String toFileLine() {
        return getTypeCode() + "|" + baseFilePart() + "|" + tracksCount;
    }

    @Override
    public String toString() {
        return super.toString() + "\nTracks count: " + tracksCount;
    }
}
