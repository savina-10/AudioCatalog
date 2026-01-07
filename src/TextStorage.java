import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextStorage {
    public void saveCatalog(String path, List<AudioItem> items) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (AudioItem a : items) {
                bw.write(a.toFileLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving catalog: " + e.getMessage());
        }
    }

    public List<AudioItem> loadCatalog(String path) {
        List<AudioItem> items = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()){
            return items;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()){
                    continue;
                }

                AudioItem a = parseCatalogLine(line);
                if (a != null){
                    items.add(a);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading catalog: " + e.getMessage());
        }

        return items;
    }

    private AudioItem parseCatalogLine(String line) {
        String[] p = line.split("\\|");
        if (p.length != 9) return null;

        String type = p[0].trim();
        int id = safeInt(p[1]);
        String title = p[2];
        String author = p[3];
        String genre = p[4];
        String duration = p[5];
        String category = p[6];
        int year = safeInt(p[7]);
        String extra = p[8];

        if (id <= 0 || year == Integer.MIN_VALUE){
            return null;
        }

        return switch (type) {
            case "SONG" -> new Song(id, title, author, genre, duration, category, year, extra);
            case "PODCAST" -> {
                int ep = safeInt(extra);
                if (ep == Integer.MIN_VALUE) {
                    yield null;
                }
                yield new Podcast(id, title, author, genre, duration, category, year, ep);
            }
            case "AUDIOBOOK" -> new Audiobook(id, title, author, genre, duration, category, year, extra);
            case "ALBUM" -> {
                int tracks = safeInt(extra);
                if (tracks == Integer.MIN_VALUE){
                    yield null;
                }
                yield new Album(id, title, author, genre, duration, category, year, tracks);
            }
            default -> null;
        };
    }

    public void savePlaylists(String path, List<Playlist> playlists) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Playlist p : playlists) {
                bw.write("PLAYLIST|" + p.getName());
                bw.newLine();
                for (int id : p.getItemIds()) {
                    bw.write("ITEM|" + id);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving playlists: " + e.getMessage());
        }
    }

    public List<Playlist> loadPlaylists(String path) {
        List<Playlist> playlists = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()){
            return playlists;
        }

        Playlist current = null;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()){
                    continue;
                }

                String[] p = line.split("\\|");
                if (p.length < 2){
                    continue;
                }

                if (p[0].equals("PLAYLIST")) {
                    current = new Playlist(p[1]);
                    playlists.add(current);
                } else if (p[0].equals("ITEM")) {
                    if (current != null) {
                        int id = safeInt(p[1]);
                        if (id > 0) current.addItemId(id);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading playlists: " + e.getMessage());
        }

        return playlists;
    }


    private int safeInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return Integer.MIN_VALUE;
        }
    }
}
