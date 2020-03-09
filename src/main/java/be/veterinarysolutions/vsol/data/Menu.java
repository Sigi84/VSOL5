package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.tools.Str;

import java.util.TreeSet;

public class Menu {

    private TreeSet<Tooth> teeth = new TreeSet<>();
    private int nr;
    private boolean ready = false;

    public Menu(TreeSet<Tooth> teeth, int nr, boolean ready) {
        this.teeth = teeth;
        this.nr = nr;
        this.ready = ready;
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

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }


}
