/*import java.util.ArrayList;

public class Bucket<K, V> { //크기 비교를 위해
    private ArrayList<K> keys;
    private ArrayList<V> vals;
    private int nbits;
    private int name;


    public Bucket(int capacity, int nbits, int name) {
        keys = new ArrayList<>();
        vals = new ArrayList<>();
        this.nbits = nbits;
        this.name = name;
    }

    public V get(K key){
        if (isEmpty()) return null;
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(key))
                return vals.get(i);
        }
        return null;
    }

    public void put(K key, V value) {
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(key)) {
                vals.set(i, value);
                return;
            }
        }
        keys.add(key);
        vals.add(value);
    }

    public void remove(K key) {
        int index = keys.indexOf(key);
        if (index != -1) {
            keys.remove(index);
            vals.remove(index);
        }
    }

    public Iterable<K> keys() {
        return new ArrayList<>(keys);
    }

    public void clear(){
        keys.clear();
        vals.clear();
    }

    public void setNbits(int nbit) { nbits = nbit;}
    public int getNbits() {return nbits;}
    public int getName() {return name;}
    public boolean isEmpty() {return keys.size() == 0;}
    public int size() {return keys.size();}
}*/