public class Audiobook extends AudioItem {
    private final String narrator;

    public Audiobook(int id, String title, String author, String genre,
                     String duration, String category, int year,
                     String narrator) {
        super(id, title, author, genre, duration, category, year);
        this.narrator = narrator;
    }

    public String getNarrator() {
        return narrator;
    }

    @Override
    public String getTypeCode() {
        return "AUDIOBOOK";
    }

    @Override
    public String toFileLine() {
        return getTypeCode() + "|" + baseFilePart() + "|" + narrator;
    }

    @Override
    public String fullString() {
        return super.fullString() + "\nNarrator: " + narrator;
    }
}
