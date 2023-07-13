public class For{
    public static void main(String[] args) {
        System.out.println(6.7 / 2);
        for(int i = 1;i<10;i++){
            for(int j = 1;j<i+1;j++){
                System.out.print(i + " * " + j + " = " + i*j + ";" + "\t\t");
            }
            System.out.println("\n");
        }

    }
}

class ForArray{
    public static void main(String[] args) {
        int[] a = new int[5];
        System.out.println(a);

        double[] array = {1,9,2.1,2,9.2};
        for (int i = 0 ; i < array.length ; i++){
            System.out.println(array[i]);
        }
    }
}

// 冒泡
class Mp{
    public static void main(String[] args) {
        double[] arr = {0,0.01,876,43,9.8,2.9,99.2,20,1,23,8};
        double temp;
        for(int i = 0 ; i < arr.length ; i++){
            for (int j = i+1 ;j<arr.length;j++){
                if(arr[j] < arr[i]){
                    temp  = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        for (int i = 0 ; i < arr.length ; i++){
            System.out.println(arr[i]);
        }
    }
}

//while
class While {
    public static void main(String[] args) {
        int count = 1000;
        while(true){
            if( count == 0) break;
            int one = count / 100;
            int tow = count % 100 / 10;
            int three = count % 10;
            if(Math.pow(one,3) + Math.pow(tow,3) + Math.pow(three,3) == count ){
                System.out.println(count);
            }
            count--;
        }
    }
}


//数组反转
class FZ{
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8,9,10};

        int len = arr.length;
        int temp;
        for (int i = 0 ; i<len / 2;i++){
            temp = arr[i];
            arr[i] = arr[len-1-i];
            arr[len-1-i] = temp;
        }

        for (int i = 0;i<arr.length;i++){
            System.out.println(arr[i]);
        }
    }
}