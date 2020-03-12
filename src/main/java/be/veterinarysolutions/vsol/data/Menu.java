package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.tools.Cal;
import be.veterinarysolutions.vsol.tools.Str;

import java.util.TreeSet;

public class Menu implements Comparable<Menu> {

    private TreeSet<Tooth> teeth = new TreeSet<>();
    private long id;
    private boolean selected, next, deleted;
    private Picture pic;

    public Menu(TreeSet<Tooth> teeth) {
        this.teeth = teeth;
        for (Tooth tooth : teeth) {
            tooth.getMenus().add(this);
        }
        id = Cal.getId();
    }

    public Menu(long id, Tooth tooth) {
        teeth.add(tooth);
        tooth.getMenus().add(this);
        this.id = id;
    }

    public void selfDestruct() {
        for (Tooth tooth : teeth) {
            tooth.setSelected(false);
            tooth.getMenus().remove(this);
        }
    }

    // GETTERS

    public TreeSet<Tooth> getTeeth() {
        return teeth;
    }

    public long getId() {
        return id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean hasPic() {
        return pic != null;
    }

    public Picture getPic() {
        return pic;
    }

    public void setPic(Picture pic) {
        this.pic = pic;
    }

    // OVERRIDES

    @Override
    public String toString() {
        if (teeth.size() == 0) {
            return "---";
        } else if (teeth.size() == 1) {
            return teeth.first().getName();
        } else {
            int first = Integer.parseInt(teeth.first().getName());
            int last = Integer.parseInt(teeth.last().getName());

            if (last - first == teeth.size() - 1) {
                return teeth.first().getName() + "-" + teeth.last().getName();
            } else {
                return Str.getList(teeth);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof  Menu) {
            return ((Menu) o).id == id;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public int compareTo(Menu o) {
        return Long.compare(id, o.id);
    }
}
