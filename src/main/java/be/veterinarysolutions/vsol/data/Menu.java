package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.tools.Cal;
import be.veterinarysolutions.vsol.tools.Str;

import java.util.TreeSet;

public class Menu {

    private TreeSet<Tooth> teeth = new TreeSet<>();
    private long id;
    private boolean selected;
    private Tooth.Status status = Tooth.Status.NONE;
    private Picture pic;

    public Menu(TreeSet<Tooth> teeth) {
        this.teeth = teeth;
        id = Cal.getId();
    }

    public Menu(Tooth tooth) {
        teeth.add(tooth);
        id = Cal.getId();
    }

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
        for (Tooth tooth : teeth) {
            tooth.setSelected(selected);
        }
    }

    public Tooth.Status getStatus() {
        return status;
    }

    public void setStatus(Tooth.Status status) {
        this.status = status;
        for (Tooth tooth : teeth) {
            tooth.setStatus(status);
        }
    }

    public Picture getPic() {
        return pic;
    }

    public void setPic(Picture pic) {
        this.pic = pic;
    }
}
