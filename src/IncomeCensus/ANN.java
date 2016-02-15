package IncomeCensus;
import java.io.*;

/*********************************************************************%
% the class ANN is an artificial neural network that is used to test %
% and train the connection weights that the EA produces and returns  %
 * a fitness value.                                                   %
% Stephen Hyde - 3603453                                             %
% Cosc 4p76                                                          %
% Final Project                                                      %
 **********************************************************************/
public class ANN {

    private double[][] weightInHid;  //represents weights between input and hidden
    private double[] weightHidOut;   //represents weights between hidden and out
    private double[][] input;     //represents inputs for each file
    private double[][] hidden;    //represents hidden for each file
    private double[] fileSolutions;  //solutions to file
    private double[] trainSolutions;  //solutions to training
    private String genotype;          //genotype of weights
    private static int numTrain = 32561; //number of training examples
    private static int numHid = 3;    //static number of hidden nodes
    private int numInp;           //number of inputs
    private File theFile;             //file of training examples
    private double fitness;           //fitness of net
    private double[][] testInput;     //array for test inputs
    private double[] testSolutions;      //array to store test solututions

    public ANN() {
        weightInHid = new double[11][3];
        weightHidOut = new double[3];
        input = new double[20][15];
        hidden = new double[20][3];
        numInp = 15;
        fileSolutions = new double[20];
        trainSolutions = new double[20];
        fitness = 0;
        genotype = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0";
        convGeno();
        trainingFitness();
        meanSquare();

    }

    public ANN(int in, int hid, String geno,ParseFile p) {
        weightInHid = new double[in][hid];
        weightHidOut = new double[hid];
        ParseFile parseFile = new ParseFile();
        input = p.getInputs();
        hidden = new double[numTrain][3];
        numInp = in;
        genotype = geno;
        fileSolutions = p.getSolutions();
        trainSolutions = new double[numTrain];
        fitness = 0;
        convGeno();
        trainingFitness();
        meanSquare();
    }
    //Finds each solution of the training files

    public void trainingFitness() {
        for (int i = 0; i < numTrain; i++) {
            trainSolutions[i] = calculateOutput(i);
        }
    }
    //finds output

    public double calculateOutput(int f) {
        double out = 0;
        int file = f;

        for (int i = 0; i < numHid; i++) {
            hidden[f][i] = activation(summation(file, i));
        }

        for (int j = 0; j < numHid; j++) {
            out += hidden[f][j] * weightHidOut[j];
        }
        out = activation(out);
       // System.out.println(" first out " + out);
        if(out > .5)
            out = 1;
        if(out <= .5)
            out = 0;
        //System.out.println(" second out " + out);
        return out;
    }
    //takes the summation of all weights to hidden

    public double summation(int f, int h) {
        double sum = 0;
        int hid = h;
        for (int i = 0; i < numInp; i++) {
            sum += input[f][i] * weightInHid[i][h];
        }
        return sum;
    }
    //calculates meansquare of all examples

    public void meanSquare() {
        double error = 0;

        for (int i = 0; i < numTrain; i++) {
            error += Math.sqrt(Math.pow((fileSolutions[i] - trainSolutions[i]), 2));
        }
        error /= numTrain;

        fitness = error;
    }
    /*this method takes a geno strings and converts it to a string array
    which will make for easier processing of neural network*/

    public void convGeno() {
        int sign = 0;
        int k = 1;
      //  System.out.println("geno " + genotype);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < numInp; j++) {
                String byt = "";
                while (genotype.charAt(k) != ' ') {
                    byt += genotype.charAt(k);
                    k++;
                }
                int value = Integer.parseInt(byt, 2);
                weightInHid[j][i] = value;
                weightInHid[j][i] /= 1023;
                if(genotype.charAt(sign) == '1')
                    weightInHid[j][i] *= -1;
               // System.out.println("Connection in->hid " + j + " " + i + " " + weightInHid[j][i]);
                sign = k + 1;
                k = k + 2;
            }
        }
        for (int i = 0; i < 3; i++) {
            String byt = "";
            while (genotype.charAt(k) != ' ') {
                byt += genotype.charAt(k);
                k++;
            }
            int value = Integer.parseInt(byt, 2);
            weightHidOut[i] = value;
            weightHidOut[i] /= 1023;
             if(genotype.charAt(sign) == '1')
                    weightHidOut[i] *= -1;
            // System.out.println("Connection hid->out " + i + " " + weightHidOut[i]);
            sign = k + 1;
            k = k + 2;
        }
    }
    //this returns the activation of a function

    public double activation(double x) {
        return 1 / (1 + (Math.pow(Math.E, (-1 * x))));
    }
    //this returns the genotype of the net

    public String getGeno() {
        return genotype;
    }
    //this returns the fitness of the net

    public double getFit() {
        return fitness;
    }

    public double testScore() {
        testInput = new double[5][11];
        testSolutions = new double[5];
        double[] netSol = new double[5];
        double score = 0;
        int k = (numTrain - 5);
        for (int i = 0; i < 5; i++) {
            testSolutions[i] = fileSolutions[(k + i)];
            for (int j = 0; j < numInp; j++) {
                testInput[i][j] = input[(k + i)][j];
            }
        }
        for (int i = 0; i < 5; i++) {
            if (calculateOutput((k + i)) > .5) {
                netSol[i] = 1;
            } else {
                netSol[i] = 0;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (Math.pow((netSol[i] - testSolutions[i]), score) == 0) {
                score++;
            }
        }
        score = score / 5;
        return score;
    }
    //method to parse training examples into array

}
