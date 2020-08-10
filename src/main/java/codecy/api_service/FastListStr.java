package codecy.api_service;

import java.util.ArrayList;

public class FastListStr {
    private ArrayList<String> list;

    public FastListStr() {
        list = new ArrayList<String>();
    }

    public FastListStr add(String data) {
        list.add(data);
        return this;
    }

    public ArrayList<String> get() {
        return list;
    }

    public static FastListStr create(String data) {
        FastListStr fastList = new FastListStr();
        return fastList.add(data);
    }

    public static ArrayList<String> get(String data) {
        FastListStr fastList = new FastListStr();
        return fastList.add(data).get();
    }

    public Object[] rules() {
        return list.toArray();
    }
}