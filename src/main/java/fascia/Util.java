package fascia;


import java.util.List;

public class Util {

    public static int choose(int n, int k){

        if( n < k){
            return 0;
        }else{
            return factorial(n) / (factorial(k) * factorial(n - k));
        }
    }

    public static int factorial(int x){
        if( x <= 0){
            return 1;
        }else{
            return (x == 1 ? x: x * factorial(x -1));
        }
    }

    public static int get_max(List<Integer> arr1, List<Integer> arr2){
        int maximum = 0;
        int size = arr1.size();

        for(int i =0;  i < size; i++){
            if( maximum < arr1.get(i)){
                maximum = arr1.get(i);
            }
            if( maximum < arr2.get(i)){
                maximum = arr2.get(i);
            }
        }
        return maximum;
    }

    public static int[] dynamic_to_static(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
}
