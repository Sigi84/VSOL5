package be.veterinarysolutions.vsol.data;

import java.util.Vector;

public class Canine {

    public static Tooth get101() { return new Tooth("101", 0.2998, 0.3279, 0.0014, 0.0771); }
    public static Tooth get102() { return new Tooth("102", 0.2635, 0.2916, 0.0114, 0.0900); }
    public static Tooth get103() { return new Tooth("103", 0.2014, 0.2646, 0.0271, 0.1300); }
    public static Tooth get104() { return new Tooth("104", 0.1311, 0.2529, 0.1157, 0.3543); }
    public static Tooth get105() { return new Tooth("105", 0.1593, 0.1932, 0.3800, 0.4400); }
    public static Tooth get106() { return new Tooth("106", 0.1241, 0.1733, 0.4557, 0.5386); }
    public static Tooth get107() { return new Tooth("107", 0.0644, 0.1335, 0.5400, 0.6371); }
    public static Tooth get108() { return new Tooth("108", 0.0035, 0.1148, 0.6500, 0.8043); }
    public static Tooth get109() { return new Tooth("109", 0.0023, 0.1253, 0.8186, 0.9371); }
    public static Tooth get110() { return new Tooth("110", 0.0433, 0.1405, 0.9329, 0.9943); }

    public static Tooth get201() { return new Tooth("201", 0., 0., 0., 0.); }
    public static Tooth get202() { return new Tooth("202", 0., 0., 0., 0.); }
    public static Tooth get203() { return new Tooth("203", 0., 0., 0., 0.); }
    public static Tooth get204() { return new Tooth("204", 0., 0., 0., 0.); }
    public static Tooth get205() { return new Tooth("205", 0., 0., 0., 0.); }
    public static Tooth get206() { return new Tooth("206", 0., 0., 0., 0.); }
    public static Tooth get207() { return new Tooth("207", 0., 0., 0., 0.); }
    public static Tooth get208() { return new Tooth("208", 0., 0., 0., 0.); }
    public static Tooth get209() { return new Tooth("209", 0., 0., 0., 0.); }
    public static Tooth get210() { return new Tooth("210", 0., 0., 0., 0.); }

    public static Tooth get301() { return new Tooth("301", 0., 0., 0., 0.); }
    public static Tooth get302() { return new Tooth("302", 0., 0., 0., 0.); }
    public static Tooth get303() { return new Tooth("303", 0., 0., 0., 0.); }
    public static Tooth get304() { return new Tooth("304", 0., 0., 0., 0.); }
    public static Tooth get305() { return new Tooth("305", 0., 0., 0., 0.); }
    public static Tooth get306() { return new Tooth("306", 0., 0., 0., 0.); }
    public static Tooth get307() { return new Tooth("307", 0., 0., 0., 0.); }
    public static Tooth get308() { return new Tooth("308", 0., 0., 0., 0.); }
    public static Tooth get309() { return new Tooth("309", 0., 0., 0., 0.); }
    public static Tooth get310() { return new Tooth("310", 0., 0., 0., 0.); }
    public static Tooth get311() { return new Tooth("311", 0., 0., 0., 0.); }

    public static Tooth get401() { return new Tooth("401", 0., 0., 0., 0.); }
    public static Tooth get402() { return new Tooth("402", 0., 0., 0., 0.); }
    public static Tooth get403() { return new Tooth("403", 0., 0., 0., 0.); }
    public static Tooth get404() { return new Tooth("404", 0., 0., 0., 0.); }
    public static Tooth get405() { return new Tooth("405", 0., 0., 0., 0.); }
    public static Tooth get406() { return new Tooth("406", 0., 0., 0., 0.); }
    public static Tooth get407() { return new Tooth("407", 0., 0., 0., 0.); }
    public static Tooth get408() { return new Tooth("408", 0., 0., 0., 0.); }
    public static Tooth get409() { return new Tooth("409", 0., 0., 0., 0.); }
    public static Tooth get410() { return new Tooth("410", 0., 0., 0., 0.); }
    public static Tooth get411() { return new Tooth("411", 0., 0., 0., 0.); }

    public static Vector<Tooth> getTeeth(boolean left, boolean top) {
        Vector<Tooth> result = new Vector<>();

        if (left && top) { // Q1
            result.add(get101());
            result.add(get102());
            result.add(get103());
            result.add(get104());
            result.add(get105());
            result.add(get106());
            result.add(get107());
            result.add(get108());
            result.add(get109());
            result.add(get110());
        } else if (!left && top) { // Q2
            result.add(get201());
            result.add(get202());
            result.add(get203());
            result.add(get204());
            result.add(get205());
            result.add(get206());
            result.add(get207());
            result.add(get208());
            result.add(get209());
            result.add(get210());
        } else if (!left && !top) { // Q3
            result.add(get301());
            result.add(get302());
            result.add(get303());
            result.add(get304());
            result.add(get305());
            result.add(get306());
            result.add(get307());
            result.add(get308());
            result.add(get309());
            result.add(get310());
            result.add(get311());
        } else if (left && !top) { // Q4
            result.add(get401());
            result.add(get402());
            result.add(get403());
            result.add(get404());
            result.add(get405());
            result.add(get406());
            result.add(get407());
            result.add(get408());
            result.add(get409());
            result.add(get410());
            result.add(get411());
        }


        return result;
    }

}
