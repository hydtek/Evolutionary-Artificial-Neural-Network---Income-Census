package IncomeCensus;
/*********************************************************************%
 % This project combines evolutionary algorithms with Artificial      %
 % Neural networks to form an Evolutionary Artificial Neural Network  %
 % to Calculate if income is above 50k or below 50k                   %
 % Stephen Hyde - 3603453                                             %
 %                                                                    %
 % Final Project                                                      %
 **********************************************************************/
import java.io.*;
public class Main {
    private EA seq;
    public Main(){
        String inp = "";
        DataInputStream in = new DataInputStream(System.in);
        System.out.println("Please Enter an Integer that represents the Population Size");
        System.out.println();
        try {
               inp = in.readLine();
           }
   	   catch (Exception e) {
                System.out.println("Invalid Input :");
            }
        seq = new EA(Integer.parseInt(inp));
    }
    public static void main(String[] args) { new Main();
    }
}
