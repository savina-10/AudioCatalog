import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Catalog {
    private List<AudioItem> items = new ArrayList<>();
    private List<Playlist> playlists = new ArrayList<>();
    private int nextId = 1;

    public List<AudioItem> getItems() {
        return items;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setItems(List<AudioItem> items) {
        this.items = (items != null) ? items : new ArrayList<>();
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = (playlists != null) ? playlists : new ArrayList<>();
    }

    public void addItem(AudioItem item) {
        items.add(item);
    }

    public AudioItem findItemById(int id) {
        for (AudioItem i : items) {
            if (i.getId() == id) return i;
        }
        return null;
    }
    public AudioItem removeItemById(int id) {
        Iterator<AudioItem> it = items.iterator();
        while (it.hasNext()) {
            AudioItem a = it.next();
            if (a.getId() == id) {
                it.remove();
                return a;
            }
        }
        return null;
    }

    public Playlist findPlaylistByName(String name) {
        for (Playlist p : playlists) {
            if (p.getName().equalsIgnoreCase(name)){
                return p;
            }
        }
        return null;
    }

    public int generateId() {
        return nextId++;
    }

    public void recalculateNextId() {
        int max = 0;
        for (AudioItem i : items) {
            if (i.getId() > max) {
                max = i.getId();
            }
        }
        nextId = max + 1;
    }
}
