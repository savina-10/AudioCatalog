import java.util.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final Scanner sc = new Scanner(System.in);


    private static void addNewItem(Catalog catalog) {
        System.out.println("\n--- ADD NEW ITEM ---");
        System.out.println("1) Song");
        System.out.println("2) Podcast");
        System.out.println("3) Audiobook");
        System.out.println("4) Album");
        int t = readInt("Type: ");

        String title = readNonEmpty("Title: ");
        String author = readNonEmpty("Author/Artist: ");
        String genre = readNonEmpty("Genre: ");
        String duration = readNonEmpty("Duration: ");
        String category = readNonEmpty("Category: ");
        int year = readInt("Year: ");

        int id = catalog.generateId();
        AudioItem item;

        switch (t) {
            case 1 -> {
                String albumName = readNonEmpty("Album name: ");
                if (containsPipe(title, author, genre, duration, category, albumName)) {
                    System.out.println("Error: fields must NOT contain '|'.");
                    return;
                }
                item = new Song(id, title, author, genre, duration, category, year, albumName);
            }
            case 2 -> {
                int ep = readInt("Episode number: ");
                if (containsPipe(title, author, genre, duration, category)) {
                    System.out.println("Error: fields must NOT contain '|'.");
                    return;
                }
                item = new Podcast(id, title, author, genre, duration, category, year, ep);
            }
            case 3 -> {
                String narrator = readNonEmpty("Narrator: ");
                if (containsPipe(title, author, genre, duration, category, narrator)) {
                    System.out.println("Error: fields must NOT contain '|'.");
                    return;
                }
                item = new Audiobook(id, title, author, genre, duration, category, year, narrator);
            }
            case 4 -> {
                int tracks = readInt("Tracks count: ");
                if (containsPipe(title, author, genre, duration, category)) {
                    System.out.println("Error: fields must NOT contain '|'.");
                    return;
                }
                item = new Album(id, title, author, genre, duration, category, year, tracks);
            }
            default -> {
                System.out.println("Invalid type.");
                return;
            }
        }

        catalog.addItem(item);
        System.out.println("Added: " + item);
    }

    private static void deleteItem(Catalog catalog) {
        System.out.println("\n--- DELETE ITEM ---");
        int id = readInt("ID to delete: ");

        AudioItem removed = catalog.removeItemById(id);
        if (removed == null) {
            System.out.println("No item with ID " + id);
            return;
        }

        for (Playlist p : catalog.getPlaylists()) {
            p.removeItemId(id);
        }

        System.out.println("Deleted: " + removed);
    }

    private static void searchItems(Catalog catalog) {
        System.out.println("\n--- SEARCH ---");
        System.out.println("1) By title (contains)");
        System.out.println("2) By author/artist (contains)");
        System.out.println("3) By genre (contains)");
        System.out.println("4) By category (contains)");
        System.out.println("5) By year (equals)");
        int ch = readInt("Search type: ");

        List<AudioItem> results;

        switch (ch) {
            case 1 -> {
                String q = readNonEmpty("Title contains: ").toLowerCase();
                results = catalog.getItems().stream()
                        .filter(i -> i.getTitle().toLowerCase().contains(q))
                        .toList();
            }
            case 2 -> {
                String q = readNonEmpty("Author contains: ").toLowerCase();
                results = catalog.getItems().stream()
                        .filter(i -> i.getAuthor().toLowerCase().contains(q))
                        .toList();
            }
            case 3 -> {
                String q = readNonEmpty("Genre contains: ").toLowerCase();
                results = catalog.getItems().stream()
                        .filter(i -> i.getGenre().toLowerCase().contains(q))
                        .toList();
            }
            case 4 -> {
                String q = readNonEmpty("Category contains: ").toLowerCase();
                results = catalog.getItems().stream()
                        .filter(i -> i.getCategory().toLowerCase().contains(q))
                        .toList();
            }
            case 5 -> {
                int y = readInt("Year equals: ");
                results = catalog.getItems().stream()
                        .filter(i -> i.getYear() == y)
                        .toList();
            }
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        printItems(results);
    }

    private static void filterItems(Catalog catalog) {
        System.out.println("\n--- FILTER ---");
        System.out.println("Leave empty to skip a filter.");

        System.out.print("Genre (contains): ");
        String genreQ = sc.nextLine().trim().toLowerCase();

        System.out.print("Author (contains): ");
        String authorQ = sc.nextLine().trim().toLowerCase();

        System.out.print("Category (contains): ");
        String categoryQ = sc.nextLine().trim().toLowerCase();

        System.out.print("Year (0 = skip): ");
        int year = 0;
        try {
            String y = sc.nextLine().trim();
            if (!y.isEmpty()) {
                year = Integer.parseInt(y);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid year. Using 0 (skip)." );
            year = 0;
        }

        int finalYear = year;
        List<AudioItem> results = catalog.getItems().stream()
                .filter(item -> genreQ.isEmpty() || item.getGenre().toLowerCase().contains(genreQ))
                .filter(item -> authorQ.isEmpty() || item.getAuthor().toLowerCase().contains(authorQ))
                .filter(item -> categoryQ.isEmpty() || item.getCategory().toLowerCase().contains(categoryQ))
                .filter(item -> finalYear == 0 || item.getYear() == finalYear)
                .toList();

        printItems(results);
    }

    private static void sortItems(Catalog catalog) {
        System.out.println("\n--- SORT ---");
        System.out.println("1) By title (A-Z)");
        System.out.println("2) By author (A-Z)");
        System.out.println("3) By year (ascending)");
        int ch = readInt("Sort type: ");

        Comparator<AudioItem> cmp;
        switch (ch) {
            case 1 -> cmp = Comparator.comparing(a -> a.getTitle().toLowerCase());
            case 2 -> cmp = Comparator.comparing(a -> a.getAuthor().toLowerCase());
            case 3 -> cmp = Comparator.comparingInt(AudioItem::getYear);
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        List<AudioItem> sorted = catalog.getItems().stream()
                .sorted(cmp)
                .toList();

        printItems(sorted);
    }

    private static void showItemOrPlaylistInfo(Catalog catalog) {
        System.out.println("\n--- INFO ---");
        System.out.println("1) Item by ID");
        System.out.println("2) Playlist by name");
        int ch = readInt("Choice: ");

        if (ch == 1) {
            int id = readInt("ID: ");
            AudioItem item = catalog.findItemById(id);
            if (item == null){
                System.out.println("Not found.");
            }
            else{
                System.out.println(item);
            }
        } else if (ch == 2) {
            String name = readNonEmpty("Playlist name: ");
            Playlist p = catalog.findPlaylistByName(name);
            if (p == null){
                System.out.println("Not found.");
            }
            else{
                System.out.println(p.infoString(catalog));
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void printItems(List<AudioItem> items) {
        if (items.isEmpty()) {
            System.out.println("(No results)");
            return;
        }
        System.out.println("Results: " + items.size());
        items.forEach(i -> System.out.println(" - " + i.toString()));
    }


    private static void playlistMenu(Catalog catalog) {
        while (true) {
            System.out.println("\n--- PLAYLIST MENU ---");
            System.out.println("1) Create playlist");
            System.out.println("2) Delete playlist");
            System.out.println("3) Add item to playlist");
            System.out.println("4) Remove item from playlist");
            System.out.println("5) Show playlist info");
            System.out.println("6) Sort playlist items by title (view)");
            System.out.println("7) List all playlists");
            System.out.println("0) Back");
            int ch = readInt("Choice: ");

            switch (ch) {
                case 1 -> createPlaylist(catalog);
                case 2 -> deletePlaylist(catalog);
                case 3 -> addItemToPlaylist(catalog);
                case 4 -> removeItemFromPlaylist(catalog);
                case 5 -> showPlaylistInfo(catalog);
                case 6 -> showPlaylistSortedByTitle(catalog);
                case 7 -> listPlaylists(catalog);
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void createPlaylist(Catalog catalog) {
        String name = readNonEmpty("New playlist name: ");
        if (containsPipe(name)) {
            System.out.println("Name must NOT contain '|'.");
            return;
        }
        if (catalog.findPlaylistByName(name) != null) {
            System.out.println("Playlist already exists.");
            return;
        }
        catalog.getPlaylists().add(new Playlist(name));
        System.out.println("Created playlist: " + name);
    }

    private static void deletePlaylist(Catalog catalog) {
        String name = readNonEmpty("Playlist to delete: ");
        Playlist p = catalog.findPlaylistByName(name);
        if (p == null) {
            System.out.println("Not found.");
            return;
        }
        catalog.getPlaylists().remove(p);
        System.out.println("Deleted playlist: " + name);
    }

    private static void addItemToPlaylist(Catalog catalog) {
        String name = readNonEmpty("Playlist name: ");
        Playlist p = catalog.findPlaylistByName(name);
        if (p == null) {
            System.out.println("Playlist not found.");
            return;
        }

        int id = readInt("Item ID to add: ");
        AudioItem item = catalog.findItemById(id);
        if (item == null) {
            System.out.println("No item with that ID.");
            return;
        }

        if (p.containsId(id)) {
            System.out.println("Already in playlist.");
            return;
        }

        p.addItemId(id);
        System.out.println("Added: " + item + " to " + p.getName());
    }

    private static void removeItemFromPlaylist(Catalog catalog) {
        String name = readNonEmpty("Playlist name: ");
        Playlist p = catalog.findPlaylistByName(name);
        if (p == null) {
            System.out.println("Playlist not found.");
            return;
        }

        int id = readInt("Item ID to remove: ");
        boolean r = p.removeItemId(id);
        if (!r){
            System.out.println("This ID is not in the playlist.");
        }
        else System.out.println("Removed ID " + id + " from " + p.getName());
    }

    private static void showPlaylistInfo(Catalog catalog) {
        String name = readNonEmpty("Playlist name: ");
        Playlist p = catalog.findPlaylistByName(name);
        if (p == null) {
            System.out.println("Not found.");
            return;
        }
        System.out.println(p.infoString(catalog));
    }

    private static void showPlaylistSortedByTitle(Catalog catalog) {
        String name = readNonEmpty("Playlist name: ");
        Playlist p = catalog.findPlaylistByName(name);
        if (p == null) {
            System.out.println("Not found.");
            return;
        }

        List<AudioItem> sorted = p.getItemIds().stream()
                .map(catalog::findItemById)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(a -> a.getTitle().toLowerCase()))
                .toList();

        if (sorted.isEmpty()) {
            System.out.println("(Playlist has no valid items)");
            return;
        }

        System.out.println("Playlist '" + p.getName() + "' sorted by title:");
        sorted.forEach(it -> System.out.println(" - " + it.toString()));
    }




    private static void listPlaylists(Catalog catalog) {
        if (catalog.getPlaylists().isEmpty()) {
            System.out.println("(No playlists)");
            return;
        }
        System.out.println("Playlists:");
        for (Playlist p : catalog.getPlaylists()) {
            System.out.println(" - " + p.getName() + " (" + p.getItemIds().size() + " items)");
        }
    }


    private static int readInt(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static String readNonEmpty(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()){
                return s;
            }
            System.out.println("Cannot be empty.");
        }
    }

    private static boolean containsPipe(String... fields) {
        for (String f : fields) {
            if (f != null && f.contains("|")){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Catalog catalog = new Catalog();
        TextStorage storage = new TextStorage();

        catalog.setItems(storage.loadCatalog("catalog.txt"));
        catalog.setPlaylists(storage.loadPlaylists("playlists.txt"));
        catalog.recalculateNextId();

        System.out.println("== Personal Audio Catalog ==");
        System.out.println("Loaded items: " + catalog.getItems().size());
        System.out.println("Loaded playlists: " + catalog.getPlaylists().size());

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1) Add new object");
            System.out.println("2) Delete object");
            System.out.println("3) Search objects");
            System.out.println("4) Filter objects");
            System.out.println("5) Sort objects");
            System.out.println("6) Playlist menu");
            System.out.println("7) Show info");
            System.out.println("8) Save catalog");
            System.out.println("9) Load catalog");
            System.out.println("10) Save playlists");
            System.out.println("11) Load playlists");
            System.out.println("0) Exit");

            int choice = readInt("Choice: ");

            switch (choice) {
                case 1 -> addNewItem(catalog);
                case 2 -> deleteItem(catalog);
                case 3 -> searchItems(catalog);
                case 4 -> filterItems(catalog);
                case 5 -> sortItems(catalog);
                case 6 -> playlistMenu(catalog);
                case 7 -> showItemOrPlaylistInfo(catalog);
                case 8 -> {
                    storage.saveCatalog("catalog.txt", catalog.getItems());
                    System.out.println("Catalog saved.");
                }
                case 9 -> {
                    catalog.setItems(storage.loadCatalog("catalog.txt"));
                    catalog.recalculateNextId();
                    System.out.println("Catalog loaded.");
                }
                case 10 -> {
                    storage.savePlaylists("playlists.txt", catalog.getPlaylists());
                    System.out.println("Playlists saved.");
                }
                case 11 -> {
                    catalog.setPlaylists(storage.loadPlaylists("playlists.txt"));
                    System.out.println("Playlists loaded.");
                }
                case 0 -> {
                    storage.saveCatalog("catalog.txt", catalog.getItems());
                    storage.savePlaylists("playlists.txt", catalog.getPlaylists());
                    System.out.println("Saved & exiting.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}