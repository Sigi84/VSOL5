package be.veterinarysolutions.vsol.data;

import java.util.Vector;

public class Feline {

    private static String type = "feline";

    public static Tooth get101() { return new Tooth(type, "101", 0.9053, 0.9905, 0.0014, 0.0742); }
    public static Tooth get102() { return new Tooth(type, "102", 0.7739, 0.8840, 0.0114, 0.0942); }
    public static Tooth get103() { return new Tooth(type, "103", 0.5964, 0.7775, 0.0342, 0.1213); }
    public static Tooth get104() { return new Tooth(type, "104", 0.3337, 0.7668, 0.1184, 0.3566); }
    public static Tooth get105() { return new Tooth(type, "105", 0.4793, 0.5858, 0.3837, 0.4451); }
    public static Tooth get106() { return new Tooth(type, "106", 0.3834, 0.5148, 0.4593, 0.5407); }
    public static Tooth get107() { return new Tooth(type, "107", 0.2024, 0.3870, 0.5478, 0.6362); }
    public static Tooth get108() { return new Tooth(type, "108", 0.0178, 0.3479, 0.6476, 0.8046); }
    public static Tooth get109() { return new Tooth(type, "109", 0.0071, 0.3870, 0.8160, 0.9401); }

    public static Tooth get201() { return new Tooth(type, "201", 0.0106, 0.0918, 0.0029, 0.0759); }
    public static Tooth get202() { return new Tooth(type, "202", 0.1200, 0.2294, 0.0100, 0.0913); }
    public static Tooth get203() { return new Tooth(type, "203", 0.2292, 0.4130, 0.0385, 0.1327); }
    public static Tooth get204() { return new Tooth(type, "204", 0.2224, 0.6601, 0.1198, 0.3609); }
    public static Tooth get205() { return new Tooth(type, "205", 0.4024, 0.5118, 0.3837, 0.4465); }
    public static Tooth get206() { return new Tooth(type, "206", 0.4871, 0.6213, 0.4593, 0.5407); }
    public static Tooth get207() { return new Tooth(type, "207", 0.6071, 0.7907, 0.5464, 0.6362); }
    public static Tooth get208() { return new Tooth(type, "208", 0.6424, 0.9778, 0.6534, 0.8031); }
    public static Tooth get209() { return new Tooth(type, "209", 0.6213, 0.9990, 0.8160, 0.9372); }

    public static Tooth get301() { return new Tooth(type, "301", 0.0047, 0.1307, 0.9158, 0.9800); }
    public static Tooth get302() { return new Tooth(type, "302", 0.1540, 0.3033, 0.9116, 0.9786); }
    public static Tooth get303() { return new Tooth(type, "303", 0.3173, 0.4807, 0.8944, 0.9643); }
    public static Tooth get304() { return new Tooth(type, "304", 0.4620, 0.9426, 0.7718, 0.9971); }
    public static Tooth get305() { return new Tooth(type, "305", 0.4293, 0.5413, 0.6933, 0.7332); }
    public static Tooth get306() { return new Tooth(type, "306", 0.4387, 0.5413, 0.6091, 0.6719); }
    public static Tooth get307() { return new Tooth(type, "307", 0.4480, 0.5973, 0.5021, 0.5763); }
    public static Tooth get308() { return new Tooth(type, "308", 0.5413, 0.7000, 0.3738, 0.4665); }
    public static Tooth get309() { return new Tooth(type, "309", 0.6300, 0.9146, 0.1883, 0.3552); }

    public static Tooth get401() { return new Tooth(type, "401", 0.8668, 0.9867, 0.9187, 0.9800); }
    public static Tooth get402() { return new Tooth(type, "402", 0.6870, 0.8391, 0.9130, 0.9786); }
    public static Tooth get403() { return new Tooth(type, "403", 0.5210, 0.6870, 0.8973, 0.9658); }
    public static Tooth get404() { return new Tooth(type, "404", 0.0000, 0.5394, 0.7718, 0.9957); }
    public static Tooth get405() { return new Tooth(type, "405", 0.4518, 0.5625, 0.6919, 0.7318); }
    public static Tooth get406() { return new Tooth(type, "406", 0.4472, 0.5487, 0.6049, 0.6719); }
    public static Tooth get407() { return new Tooth(type, "407", 0.3873, 0.5256, 0.5007, 0.5792); }
    public static Tooth get408() { return new Tooth(type, "408", 0.2951, 0.4472, 0.3766, 0.4650); }
    public static Tooth get409() { return new Tooth(type, "409", 0.0968, 0.3642, 0.1869, 0.3538); }

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
        }


        return result;
    }

}
