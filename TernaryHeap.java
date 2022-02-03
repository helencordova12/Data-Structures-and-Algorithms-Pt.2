import java.util.ArrayList;
import java.util.Collections;

public class TernaryHeap {

    public static void sort(Comparable[] pq) {
        int n = pq.length;

        // heapify phase
        for (int k = n/3; k >= 0; k--) //in my program the indices are not "off-by-one"
            sink(pq, k, n);

        // sortdown phase
        int k = n;
        while (k > 0) {
            exch(pq, 0, --k);
            sink(pq, 0, k);
        }
    }

    public static int findLargestChild(Comparable[] pq, int parent, int size){

        int leftChild = 3*parent+1;
        int middleChild = 3*parent+2;
        int rightChild = 3*parent+3;

        int index = leftChild;

        if(middleChild > size-1){
            return leftChild;
        }

        if(less(pq, leftChild, middleChild)){
            index = middleChild;
        }

        if(rightChild > size-1){
            return index;
        }

        if(less(pq, index, rightChild)){
            index = rightChild;
        }

        return index;
    }

    private static void sink(Comparable[] pq, int k, int n) { //creates max heap
        while (3*k+1 < n) {

            int j = findLargestChild(pq, k, n);

            if (!less(pq, k, j))
                break;
            exch(pq, k, j);
            k = j;
        }
    }

    private static boolean less(Comparable[] pq, int i, int j) { //compares two integers
        return pq[i].compareTo(pq[j]) < 0;
    }

    private static void exch(Object[] pq, int i, int j) { //swaps elements in array
        Object swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    private static void show(Comparable[] a) { //displays elements in array
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {

        Integer[] numbers = new Integer[100];

        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < 100; i++){
            list.add(i+1);
        }

        Collections.shuffle(list); //shuffles the numbers in list
        for (int i = 0; i < 100; i++){
            numbers[i] = Integer.valueOf(list.get(i));
        }

        sort(numbers);
        show(numbers);
    }
}