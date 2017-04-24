package fascia;



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

}
