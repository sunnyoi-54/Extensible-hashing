//22211983 전선영
import java.util.ArrayList;
import java.util.HashSet;

public class ExtensibleHashST<K,V> {
    private int N;
    private int M;
    private int i;
    private ArrayList<Bucket> dir;
    private int name;
    private int bucketSize;

    public ExtensibleHashST() { //버킷 사이즈 기본 = 4
        this.i = 0;
        this.M = (int)Math.pow(2,i);
        this.name = 0;
        this.bucketSize = 4;
        dir = new ArrayList<>(M);
        for(int i=0; i<M; i++){
            dir.add(new Bucket<K, V>(bucketSize, 0, name));
        }
    }

    public ExtensibleHashST(int bucketSize) { //버킷 사이즈 설정
        this.i = 0;
        this.M = (int)Math.pow(2,i);
        this.bucketSize = bucketSize;
        dir = new ArrayList<>(M);
        for(int j=0; j<M; j++){
            dir.add(new Bucket<K, V>(bucketSize, 0, name));
        }
    }

    private int hash(K key) {
        return key.hashCode() & ((1 << i) - 1);
    } //해시 함수

    public V get(K key) {
        return (V)dir.get(hash(key)).get(key);
    } //get 함수

    public void put(K key, V value) { //put 함수
        if(get(key) == null){ //key가 존재하지 않으면
            while(dir.get(hash(key)).size() == bucketSize) //넣을 때마다 버킷이 꽉 차 있으면 rehash 해야 됨
                rehash(key, value);
            dir.get(hash(key)).put(key, value); //안 꽉 차 있을 때 넣기
            N++;
        }
        else {
            dir.get(hash(key)).put(key, value); //key가 존재하면 value 업데이트
        }
    }

    public void rehash(K key, V value){ //재배치
        int oldIndex = hash(key); //원래 해시값

        if(dir.get(hash(key)).getNbits() == i){ //비트 수 늘리기
            i++;
            this.M = (int)Math.pow(2,i); //사이즈는 해싱하는 비트 수의 제곱
            ArrayList<Bucket> newDir = new ArrayList<>(M);
            for (int j = 0; j < M; j++){
                newDir.add(dir.get(j % dir.size())); //새롭게 확장된 버킷 연결시키기
            }
            dir = newDir;
        }

        Bucket oldBucket = dir.get(oldIndex); //옛날 해시값이랑 연결된 버킷 - 확장하고자 하는
        int nbits = oldBucket.getNbits() + 1; //이 버킷의 해시 비트 수 늘리기

        oldBucket.setNbits(nbits);
        Bucket newBucket = new Bucket<K, V>(bucketSize, nbits, ++name);

        ArrayList<K> keys = new ArrayList<>();
        ArrayList<V> values = new ArrayList<>();

        for(Object oldKey : oldBucket.keys()) { //oldbucket에 있는 것들은 재해싱해야 함
            keys.add((K)oldKey);
            values.add((V)oldBucket.get(oldKey));
        }

        oldBucket.clear();

        for (int index = 0; index < keys.size(); index++) {
            K rehashKey = keys.get(index);
            V rehashValue = values.get(index);
            if ((hash(rehashKey) & (1 << (nbits - 1))) != 0) { //해시 비트의 첫번째 비트가 1이면 새 버킷
                newBucket.put(rehashKey, rehashValue);
            } else { //비트가 0이면 원래 버킷
                oldBucket.put(rehashKey, rehashValue);
            }
        }

        for (int j = 0; j < M; j++) { //새로 생성한 버킷을 어디 달 지 찾기
            if (((j >> (newBucket.getNbits() - 1)) & 1) == 1 && compareLastNBits(j, oldIndex, newBucket.getNbits() - 1)) {
                dir.set(j, newBucket); //첫번째 비트가 0이고, 나머지 비트가 옛날 비트랑 같은 곳에 달기
            }
        }
    }

    public Iterable<K> keys() {
        ArrayList<K> allKeys = new ArrayList<>();
        for (Bucket<K, V> bucket : dir) {
            for (K key : bucket.keys()) {
                allKeys.add(key);
            }
        }
        return allKeys;
    }

    public void summaryInfo(){
        StringBuilder sb = new StringBuilder("");
        sb.append("Global i = ").append(i).append("비트, (key, value) 쌍의 수 = ").append(N)
                .append(", 버킷의 수 = ").append(name+1);
        System.out.println(sb);
    }

    public void detailInfo() {
        summaryInfo();
        StringBuilder sb = new StringBuilder("");
        for(int j=0; j<M; j++){
            sb.append("Directory [").append(j).append("] -> Bucket ").append(dir.get(j).getName()).append("\n");
        }
        HashSet<Integer> bucketNames = new HashSet<>();

        for (int i = 0; i < dir.size(); i++) {
            Bucket bucket = dir.get(i);
            if (!bucketNames.add(bucket.getName())) {
                continue;
            }
            sb.append("Bucket ").append(bucket.getName()).append(": size = ").append(bucket.size());
            sb.append(", nbits = ").append(bucket.getNbits()).append("비트\n");
            for (Object key : bucket.keys()) {
                sb.append(key).append(" : ").append(bucket.get(key)).append("\n");
            }
        }
        System.out.println(sb);
    }

    public boolean contains(K key) {return get(key) != null;}
    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}

    public static boolean compareLastNBits(int binaryA, int binaryB, int n) {
        int mask = (1 << n) - 1;

        int lastNBitsA = binaryA & mask;
        int lastNBitsB = binaryB & mask;

        return lastNBitsA == lastNBitsB;
    }
}


class Bucket<K, V> { 
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
}