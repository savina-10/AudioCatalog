public class Podcast extends AudioItem {
    private final int episodeNumber;

    public Podcast(int id, String title, String author, String genre,
                   String duration, String category, int year,
                   int episodeNumber) {
        super(id, title, author, genre, duration, category, year);
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String getTypeCode() {
        return "PODCAST";
    }

    @Override
    public String toFileLine() {
        return getTypeCode() + "|" + baseFilePart() + "|" + episodeNumber;
    }

    @Override
    public String toString() {
        return super.toString() + "\nEpisode: " + episodeNumber;
    }
}
