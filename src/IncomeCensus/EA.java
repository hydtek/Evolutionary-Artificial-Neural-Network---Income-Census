package IncomeCensus;
import IncomeCensus.ANN;
import java.io.*;
/*********************************************************************%
 % the class EA is Evolutionary Algorithm that uses GA to populate    %
 % genotypes that represent the connection weights. Uniform order     %
 % crossover and mutations were used. No elitism was used.            %
 **********************************************************************/
public class EA {
    private String[] iniPop;    //array of the initial population
    private String[] nPop;      //array of new population
    private String child1;       //array representing genotype of a child node
    private String child2;       //array representing genotype of a child node
    private int popSize;        //size of the population
    private final static int numWeights = 45;    //number of connections
    private final static int binLength = 1023;     //length of the genotype 10 bits
    private final static int genoLength = 10;     //10 bits for numeric
    private final static int numInp = 14;        //static number of inputs
    private final static int numHid = 3;         //static number of hidden nodes
    private int generations;    //stores how many generations of evolution occur
    private int crossOverRate;  //this variable stores the rate of crossover
    private int mutationRate;   //this variable stores the rate of mutation
    private final static int kSelect = 3; //Tounament selection limit variable
    private ANN bestNet;                  //stores best all time Neural net
    private ANN[] ann;                    //array to store all current nets
    private ParseFile parseFile;
    private ParseFileTest parseTestFile;
    private int test = 0;
    private int cho = 0;
    //default constructor
    public EA(){
        popSize = 20;
        iniPop = new String[popSize];
        nPop = new String[popSize];
        ann = new ANN[popSize];
        child1 = "";
        child2 = "";
        generations = 0;
        createIniPop();
        assignFitness(iniPop);
        GA(iniPop);
        parseFile = new ParseFile();
        parseTestFile = new ParseFileTest();
    }
    public EA(int size){
        popSize = size;
        iniPop = new String[popSize];
        nPop = new String[popSize];
        ann = new ANN[popSize];
        child1 = "";
        child2 = "";
        generations = 0;
        parseFile = new ParseFile();
        parseTestFile = new ParseFileTest();
        createIniPop();
        assignFitness(iniPop);
        bestNet = ann[0];

        System.out.println(" we finished original " + bestNet.getFit());
        GA(iniPop);
        //runs GA based on generations inputed from user
        for(int i = 0;i < generations; i++){
            GA(nPop);
            for(int j = 0; j < popSize;j++){
                if(bestNet.getFit() > ann[j].getFit())
                    bestNet = ann[j];
            }
            System.out.println("Best Net produced " + bestNet.getFit() + " " + i + " " + generations);
        }
        System.out.println("");
        System.out.print("Testing with best net devoloped during evolution...");
        double fit = assignTestFitness(nPop, bestNet);
        System.out.println("done");
        System.out.println("This number shows the accuracy in percentage when using best ann genotype with test data " +  fit);


    }

    //randomizes the initial population
    public void createIniPop(){
        System.out.print("creating initial population.....");
        String genotype = "";
        String input = "";
        DataInputStream in = new DataInputStream(System.in);
        for(int i = 0; i < popSize; i++){
            for(int j = 0; j < numWeights; j++){
                int sign =(int)(Math.random() * 2);
                int rand = (int)(Math.random() * binLength);

                if(sign == 0)
                    genotype += "0";
                else if(sign == 1){
                    genotype += "1";
                }
                String geno = "";
                geno += Integer.toBinaryString(rand);
                int diff = genoLength - geno.length();
                for(int d = 0; d < diff; d++){
                 geno = "0" + geno;
                }
                genotype += geno;
                genotype+=" ";
            }

            iniPop[i] = genotype;
            genotype = "";
        }

        System.out.println("Done");
        System.out.println("plz enter the amount of generations");
        System.out.println();
        try {
               input = in.readLine();
           }
   	   catch (Exception e) {
                System.out.println("Invalid Input :");
         }
        generations = Integer.parseInt(input);
        System.out.println("Please enter the CrossOver Rate in percentage form; exp-> input: 100 = 100%.");
        System.out.println();
        try {
               input = in.readLine();
           }
        catch (Exception e) {
                System.out.println("Invalid Input :");
         }
        crossOverRate = Integer.parseInt(input);
        System.out.println("Please enter the Mutation Rate in percentage form; exp-> input: 100 = 100%.");
        System.out.println();
        try {
               input = in.readLine();
           }
        catch (Exception e) {
                System.out.println("Invalid Input :");
         }
        mutationRate = Integer.parseInt(input);
    }
    //this method assigns the fitness by creating the correlating neural net
    public void assignFitness(String[] p){
       String[] pop = p;

        for(int i = 0; i < popSize; i++){
            ann[i] = new ANN(numInp,numHid,pop[i],parseFile);
        }
    }
    public Double assignTestFitness(String[] p, ANN a){
       String[] pop = p;

        for(int i = 0; i < popSize; i++){
            a = new ANN(numInp,numHid,pop[i],parseFile);
        }
       return a.getFit();
    }
    //takes care of the main sequence of the Genetic Algorithm
    public void GA (String[] p){
        String[] pop = p;
        String[] newPop = new String[p.length];
        DataInputStream in = new DataInputStream(System.in);
        String input = "";

        for(int i = 0; i < popSize; i++){
            newPop[i] = tournSelect(pop);
           // newPop[i] = rouletteSelect(pop);
        }
        for(int j = 0; j < popSize; j++){
            nPop[j] = newPop[j];
        }
        assignFitness(nPop);
    }
    /*natural selection method name tournament selection is used to find the
     ofsprings for the next generation*/
    public String tournSelect(String[] p){
        int smallDist = 0;   //smallest fit of first distribution
        int smallDist2 = 0;  //smallest fit of second distribution
        String[] pop = p;    //current population
        int choice = 0;      //random number for percentage of cross over
        int mutChoice = 0;   //random number for percentage of mutation
        int[] selected = new int[kSelect]; //array for first 3 random selected
        int[] selected2 = new int[kSelect];//array for second 3 random selected

        //if were still within bounds create offsprings

         for(int i = 0; i < kSelect;i++){
           selected[i] = (int)(Math.random() * popSize);
           for(int j = 0; j < kSelect;j++){
               if(selected[i] == selected[j] && j!=i){
                  selected[i] = (int)(Math.random() * popSize);
                  j=0;
               }
           }
        }
        smallDist = selected[0];

        for(int k = 1; k < kSelect;k++){
            if(ann[smallDist].getFit() > ann[selected[k]].getFit()){
                smallDist = selected[k];
            }

        }


        for(int s = 0; s < kSelect;s++){
           selected2[s] = (int)(Math.random() * popSize);
           for(int t = 0; t < kSelect;t++){
               if(selected2[s] == selected2[t] && t!=s){
                  selected2[s] = (int)(Math.random() * popSize);
                  t=0;
               }
           }
        }
        smallDist2 = selected2[0];
        for(int u = 1; u < kSelect; u++){
             if(ann[smallDist2].getFit() > ann[selected2[u]].getFit()){
                 smallDist2 = selected2[u];
            }
             if(smallDist2 == smallDist){
                 if(ann[smallDist2].getFit() > ann[selected2[u]].getFit()){
                    selected2[u] = (int)(Math.random() * popSize);
                     smallDist2 = selected2[u];

                 }
                 else {
                    selected2[u-1] = (int)(Math.random() * popSize);
                    smallDist2 = selected2[0];

                 }
             }
        }

        /*randomize the cross over and mutation rate
         * to define the next sequence in the GA*/
        choice = (int)(Math.random() * 100);
        mutChoice = (int)(Math.random() * 100);

        //sequence of GA
        if(choice < crossOverRate){
            // uoxCrossOver(pop[smallDist], pop[smallDist2], mutChoice);
             onePointCrossOver(pop[smallDist], pop[smallDist2], mutChoice);
           }
       else if(choice > crossOverRate && mutChoice < mutationRate)
       {
           child1 = inverseMutation(pop[smallDist]);
           child2 = inverseMutation(pop[smallDist2]);
           //child1 = userMutation(pop[smallDist]);
          // child2 = userMutation(pop[smallDist2]);

        }
       else if (choice > crossOverRate && mutChoice > mutationRate) {
            child1 = pop[smallDist];
            child2 = pop[smallDist2];
        }
         int rand = (int)(Math.random() * 4);
         if(rand == 0)
           return pop[smallDist];
         else if(rand == 1)
           return pop[smallDist2];
         else if(rand == 2)
           return child1;
         else 
           return child2;
          
    }
    //creates random bit mask for the cross over operator
    public String rouletteSelect(String[] p){
        int choiceOne = (int)(Math.random() * popSize);   //smallest fit of first distribution
        int choiceTwo = (int)(Math.random() * popSize);  //smallest fit of second distribution
        int choice = 0;      //random number for percentage of cross over
        int mutChoice = 0;   //random number for percentage of mutation
        String[] pop = p;    //current population

        choice = (int)(Math.random() * 100);
        mutChoice = (int)(Math.random() * 100);

        //sequence of GA
        if(choice < crossOverRate){
             //uoxCrossOver(pop[choiceOne], pop[choiceTwo], mutChoice);
             onePointCrossOver(pop[choiceOne], pop[choiceTwo], mutChoice);
           }
       else if(choice > crossOverRate && mutChoice < mutationRate)
       {
           child1 = userMutation(pop[choiceOne]);
           child2 = userMutation(pop[choiceTwo]);
          //child1 = inverseMutation(pop[choiceOne]);
         // child2 = inverseMutation(pop[choiceTwo]);
        }
       else if (choice > crossOverRate && mutChoice > mutationRate) {
            child1 = pop[choiceOne];
            child2 = pop[choiceTwo];
        }
         int rand = (int)(Math.random() * 4);
         if(rand == 0)
           return pop[choiceOne];
         else if(rand == 1)
           return pop[choiceTwo];
         else if(rand == 2)
           return child1;
         else
           return child2;

    }
    public String bitM(){
         String mask = "";
         for(int i = 0; i < (genoLength+1); i++){
            mask += (int)(Math.random() * 2);
         }
         return mask;
    }
    //convGeno to an integer array based representation
      public int[] convGeno(String g) {
          String genotype = g;
          int[] gType = new int[numWeights];
        int k = 1;
            for (int j = 0; j < numWeights; j++) {
                String byt = "";
                while (genotype.charAt(k) != ' ') {
                    byt += genotype.charAt(k);
                    k++;
                }
                int value = Integer.parseInt(byt, 2);
                gType[j] = value;
                k = k+1;
            }
            return gType;
        }
      //convGeno to an String based representation
      public String convGeno1(int[] g1){
          String geno = "";
          for(int j = 0; j < numWeights; j++){
                int temp = g1[j];
                geno += Integer.toBinaryString(temp);
                geno +=" ";
          }
          return geno;
      }

      //crossover operator for GA using uniform order cross over.
    public void uoxCrossOver(String g1, String g2,int mutC){

        String bitMask = "";
        String c1 = "";
        String c2 = "";
        int c = 0;
        bitMask = bitM();

        for(int j = 0; j < numWeights; j++){
        for(int i = 0; i < bitMask.length();i++){
            if(bitMask.charAt(i) == '0'){
               c1 += g1.charAt(c);
               c2 += g2.charAt(c);
            }
            else if(bitMask.charAt(i) == '1'){
                c1 += g2.charAt(c);
                c2 += g1.charAt(c);
            }
            c++;
         }
          c1 += ' ';
          c2 += ' ';
          c++;
        }
           // System.out.println(" CrossOver " + g1 + " " + g2 + " " + c1 + " " + c2 + " " + bitMask);
            child1 = c1;
            child2 = c2;
    }
    //mutation operator using inverse.
    public void onePointCrossOver(String g1, String g2, int mutC){
        int rand = (int)(Math.random() * g1.length());
        String c1 = "";
        String c2 = "";
        c1 += g1.substring(0, rand);
        c1 += g2.substring(rand, g2.length());
        c2 += g2.substring(0, rand);
        c2 += g1.substring(rand, g1.length());
        child1 = c1;
        child2 = c2;
    }

    public String inverseMutation(String g1){
        String c1 = "";

        for(int i = 0; i < g1.length(); i++ ){
           if(g1.charAt(i) == '0')
               c1 += '1';
           else if(g1.charAt(i) == '1')
               c1 += '0';
           else
               c1 += ' ';
        }
       return c1;
    }
    public String userMutation(String g1){
        String c1 = "";
        int next = 1;
        for(int i = 0; i < (g1.length()-2); i++){
            if(g1.charAt(next) == ' '){
                next++;
                c1 += g1.charAt(next);
            }
            else if(g1.charAt(i) == ' '){
                next++;
                c1 += ' ';
            }
            else{
                c1 += g1.charAt(next);
                next++;
            }
        }
        c1 += g1.charAt(0);
        c1 += " ";
        return c1;
    }
}
