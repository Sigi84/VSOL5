package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.tools.Cal;
import be.veterinarysolutions.vsol.tools.Str;

import java.util.TreeSet;

public class Menu implements Comparable<Menu> {

    public enum Status { ADDED, NEXT, FAILED, TAKEN  }

    private TreeSet<Tooth> teeth = new TreeSet<>();
    private long id;
    private boolean selected;
    private Status status = Status.ADDED;
    private Picture pic;

    public Menu(TreeSet<Tooth> teeth) {
        this.teeth = teeth;
        for (Tooth tooth : teeth) {
            tooth.getMenus().add(this);
        }
        id = Cal.getId();
    }

    public void selfDestruct() {
        setSelected(false);
        setStatus(Status.ADDED);

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        return o.status.compareTo(status);
    }
}
