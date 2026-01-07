public abstract class AudioItem {
    private final int id;
    private final String title;
    private final String author;
    private final String genre;
    private final String duration;
    private final String category;
    private final int year;

    protected AudioItem(int id, String title, String author, String genre,
                        String duration, String category, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.duration = duration;
        this.category = category;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getDuration() {
        return duration;
    }

    public String getCategory() {
        return category;
    }

    public int getYear() {
        return year;
    }

    public abstract String getTypeCode();

    protected String baseFilePart() {
        return id + "|" + title + "|" + author + "|" + genre + "|" + duration + "|" + category + "|" + year;
    }

    public abstract String toFileLine();

    @Override
    public String toString() {
        return "Type: " + getTypeCode() + "\n" +
                "ID: " + id + "\n" +
                "Title: " + title + "\n" +
                "Author: " + author + "\n" +
                "Genre: " + genre + "\n" +
                "Duration: " + duration + "\n" +
                "Category: " + category + "\n" +
                "Year: " + year;
    }
}
