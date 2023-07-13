public class For{
    public static void main(String[] args) {
        for(int i = 1;i<10;i++){
            for(int j = 1;j<i+1;j++){
                System.out.print(i + " * " + j + " = " + i*j + ";" + "\t\t");
            }
            System.out.println("\n");
        }

    }
}


class While {
    public static void main(String[] args) {
        int count = 1000;
        while(true){
            if(count==0) break;
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

