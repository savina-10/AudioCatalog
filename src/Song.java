public class Song extends AudioItem {
    private final String albumName;

    public Song(int id, String title, String author, String genre,
                String duration, String category, int year,
                String albumName) {
        super(id, title, author, genre, duration, category, year);
        this.albumName = albumName;
    }

    public String getAlbumName() {
        return albumName;
    }

    @Override
    public String getTypeCode() {
        return "SONG";
    }

    @Override
    public String toFileLine() {
        return getTypeCode() + "|" + baseFilePart() + "|" + albumName;
    }

    @Override
    public String fullString() {
        return super.fullString() + "\nAlbum: " + albumName;
    }

}
