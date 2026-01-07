
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private final String name;
    private final List<Integer> itemIds = new ArrayList<>();

    public Playlist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getItemIds() {
        return itemIds;
    }

    public void addItemId(int id) {
        itemIds.add(id);
    }

    public boolean removeItemId(int id) {
        return itemIds.remove(Integer.valueOf(id));
    }

    public boolean containsId(int id) {
        return itemIds.contains(id);
    }

    public String infoString(Catalog catalog) {
        StringBuilder sb = new StringBuilder();
        sb.append("Playlist: ").append(name).append("\n");
        sb.append("Items count: ").append(itemIds.size()).append("\n");

        if (itemIds.isEmpty()) {
            sb.append("(empty)\n");
            return sb.toString();
        }

        sb.append("Items:\n");
        for (int id : itemIds) {
            AudioItem it = catalog.findItemById(id);
            if (it == null) {
                sb.append(" - ID ").append(id).append(" (missing from catalog)\n");
            } else {
                sb.append(" - ").append(it.shortString()).append("\n");
            }
        }
        return sb.toString();
    }
}
