package net.minestom.engine.ui;

public class ImGuiUtils {
    public static String[] enumToStringArray(Class<? extends Enum<?>> clazz) {
        var ec = clazz.getEnumConstants();
        var arr = new String[ec.length];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = ec[i].name();
        }
        return arr;
    }

    public static String[] enumLikeToStringArray(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new String[0];
        }
        var ec = clazz.getFields();
        var arr = new String[ec.length];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = ec[i].getName();
        }
        return arr;
    }

    public static Object getEnumLikeIndexed(String className, int index) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new String[0];
        }
        var ec = clazz.getFields();
        try {
            var f = ec[index];
            f.setAccessible(true);
            return f.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
