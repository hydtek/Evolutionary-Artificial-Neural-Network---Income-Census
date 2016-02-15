package IncomeCensus;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stephen
 */
public class ParseFile {

    private double[] fileSolutions;  //solutions to file
    private double[][] input;     //represents inputs for each file
    private static final int numInp = 15;
    private static int numTrain = 32561; //number of training examples
    private File theFile;

    public ParseFile() {
        input = new double[numTrain][numInp];
        fileSolutions = new double[numTrain];
        parseFile();
    }

    public void parseFile() {
        String line = "";
        FileInputStream inp = null;
        BufferedInputStream buff = null;
        DataInputStream data = null;
        String temp = "";
        String origInput = "";
        int comma = 0;
        int start = 0;
        int count = 0;
        int i = 0;
        
         URL url = getClass().getResource("TrainingData.txt");
        try {
            theFile = new File(url.toURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger(ParseFileTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            inp = new FileInputStream(theFile);
            buff = new BufferedInputStream(inp);
            data = new DataInputStream(buff);

            while (data.available() != 0) {
                line = data.readLine();
                if (!line.equalsIgnoreCase("")) {
                    start = 0;
                    for (int j = 0; j < numInp; j++) {
                        if (j != (numInp - 1)) {
                            while (line.charAt(comma) != ',') {
                                comma++;
                            }
                        } else {
                            comma = line.length() - 1;
                        }

                        if (comma == line.length()) {
                            comma--;
                        }
                        origInput = line.substring(start, comma);
                        if (j < (numInp - 1)) {
                            input[count][i] = normalizeInput(i, origInput);
                        } else {
                            if (origInput.charAt(0) == '<') {
                                fileSolutions[count] = 0;
                            } else {
                                fileSolutions[count] = 1;
                            }
                        }
                        comma = comma + 2;
                        start = comma;
                        i++;
                    }
                }
                i = 0;
                start = 0;
                comma = 0;
                count++;
            }
            inp.close();
            buff.close();
            data.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double normalizeInput(int attr, String value) {
        double normal = 0;
        if (attr == 0) {
            normal = (1.0/100.0) * Double.parseDouble(value);
        } else if (attr == 1) {
            if (value.equalsIgnoreCase("?")) {
                normal = 0;
            } else if (value.equalsIgnoreCase("private")) {
                normal = 1.0 / 8.0;
            } else if (value.equalsIgnoreCase("Self-emp-not-inc")) {
                normal = 2.0 / 8.0;
            } else if (value.equalsIgnoreCase("Self-emp-inc")) {
                normal = 3.0 / 8.0;
            } else if (value.equalsIgnoreCase("Federal-gov")) {
                normal = 4.0 / 8.0;
            } else if (value.equalsIgnoreCase("Local-gov")) {
                normal = 5.0 / 8.0;
            } else if (value.equalsIgnoreCase("State-gov")) {
                normal = 6.0 / 8.0;
            } else if (value.equalsIgnoreCase("Without-pay")) {
                normal = 7.0 / 8.0;
            } else if (value.equalsIgnoreCase("Never-worked")) {
                normal = 8.0 / 8.0;
            }
        } else if (attr == 2) {
            normal = (1.0/1000000.0) * Double.parseDouble(value);
        } else if (attr == 3) {
            if (value.equalsIgnoreCase("?")) {
                normal = 0;
            } else if (value.equalsIgnoreCase("Bachelors")) {
                normal = 1.0 / 16.0;
            } else if (value.equalsIgnoreCase("Some-college")) {
                normal = 2.0 / 16.0;
            } else if (value.equalsIgnoreCase("11th")) {
                normal = 3.0 / 16.0;
            } else if (value.equalsIgnoreCase("HS-grad")) {
                normal = 4.0 / 16.0;
            } else if (value.equalsIgnoreCase("Prof-school")) {
                normal = 5.0 / 16.0;
            } else if (value.equalsIgnoreCase("Assoc-acdm")) {
                normal = 6.0 / 16.0;
            } else if (value.equalsIgnoreCase("Assoc-voc")) {
                normal = 7.0 / 16.0;
            } else if (value.equalsIgnoreCase("9th")) {
                normal = 8.0 / 16.0;
            } else if (value.equalsIgnoreCase("7th-8th")) {
                normal = 9.0 / 16.0;
            } else if (value.equalsIgnoreCase("12th")) {
                normal = 10.0 / 16.0;
            } else if (value.equalsIgnoreCase("Masters")) {
                normal = 11.0 / 16.0;
            } else if (value.equalsIgnoreCase("1st-4th")) {
                normal = 12.0 / 16.0;
            } else if (value.equalsIgnoreCase("10th")) {
                normal = 13.0 / 16.0;
            } else if (value.equalsIgnoreCase("Doctorate")) {
                normal = 14.0 / 16.0;
            } else if (value.equalsIgnoreCase("5th-6th")) {
                normal = 15.0 / 16.0;
            } else if (value.equalsIgnoreCase("Preschool")) {
                normal = 16.0 / 16.0;
            }
        } else if (attr == 4) {
            normal = (1.0/20) * Double.parseDouble(value);
        } else if (attr == 5) {
            if (value.equalsIgnoreCase("?")) {
                normal = 0;
            } else if (value.equalsIgnoreCase("Married-civ-spouse")) {
                normal = 1.0 / 7.0;
            } else if (value.equalsIgnoreCase("Divorced")) {
                normal = 2.0 / 7.0;
            } else if (value.equalsIgnoreCase("Never-married")) {
                normal = 3.0 / 7.0;
            } else if (value.equalsIgnoreCase("Separated")) {
                normal = 4.0 / 7.0;
            } else if (value.equalsIgnoreCase("Widowed")) {
                normal = 5.0 / 7.0;
            } else if (value.equalsIgnoreCase("Married-spouse-absent")) {
                normal = 6.0 / 7.0;
            } else if (value.equalsIgnoreCase("Married-AF-spouse")) {
                normal = 7.0 / 7.0;
            }
        } else if (attr == 6) {
            if (value.equalsIgnoreCase("?")) {
                normal = 0;
            } else if (value.equalsIgnoreCase("Tech-support")) {
                normal = 1.0 / 14.0;
            } else if (value.equalsIgnoreCase("Craft-repair")) {
                normal = 2.0 / 14.0;
            } else if (value.equalsIgnoreCase("Other-service")) {
                normal = 3.0 / 14.0;
            } else if (value.equalsIgnoreCase("Sales")) {
                normal = 4.0 / 14.0;
            } else if (value.equalsIgnoreCase("Exec-managerial")) {
                normal = 5.0 / 14.0;
            } else if (value.equalsIgnoreCase("Prof-specialty")) {
                normal = 6.0 / 14.0;
            } else if (value.equalsIgnoreCase("Handlers-cleaners")) {
                normal = 7.0 / 14.0;
            } else if (value.equalsIgnoreCase("Machine-op-inspct")) {
                normal = 8.0 / 14.0;
            } else if (value.equalsIgnoreCase("Adm-clerical")) {
                normal = 9.0 / 14.0;
            } else if (value.equalsIgnoreCase("Farming-fishing")) {
                normal = 10.0 / 14.0;
            } else if (value.equalsIgnoreCase("Transport-moving")) {
                normal = 11.0 / 14.0;
            } else if (value.equalsIgnoreCase("Priv-house-serv")) {
                normal = 12.0 / 14.0;
            } else if (value.equalsIgnoreCase("Protective-serv")) {
                normal = 13.0 / 14.0;
            } else if (value.equalsIgnoreCase("Armed-Forces")) {
                normal = 14.0 / 14.0;
            }
        } else if (attr == 7) {
            if (value.equalsIgnoreCase("?")) {
                normal = 0;
            } else if (value.equalsIgnoreCase("Wife")) {
                normal = 1.0 / 6.0;
            } else if (value.equalsIgnoreCase("Own-child")) {
                normal = 2.0 / 6.0;
            } else if (value.equalsIgnoreCase("Husband")) {
                normal = 3.0 / 6.0;
            } else if (value.equalsIgnoreCase("Not-in-family")) {
                normal = 4.0 / 6.0;
            } else if (value.equalsIgnoreCase("Other-relative")) {
                normal = 5.0 / 6.0;
            } else if (value.equalsIgnoreCase("Unmarried")) {
                normal = 6.0 / 6.0;
            }
        } else if (attr == 8) {
            if (value.equalsIgnoreCase("?")) {
                normal = 0;
            } else if (value.equalsIgnoreCase("White")) {
                normal = 1.0 / 5.0;
            } else if (value.equalsIgnoreCase("Asian-Pac-Islander")) {
                normal = 2.0 / 5.0;
            } else if (value.equalsIgnoreCase("Amer-Indian-Eskimo")) {
                normal = 3.0 / 5.0;
            } else if (value.equalsIgnoreCase("Other")) {
                normal = 4.0 / 5.0;
            } else if (value.equalsIgnoreCase("Black")) {
                normal = 5.0 / 5.0;
            }
         } else if (attr == 9) {
            if (value.equalsIgnoreCase("?")) {
                normal = 0;
            } else if (value.equalsIgnoreCase("Female")) {
                normal = 1.0 / 2.0;
            } else if (value.equalsIgnoreCase("Male")) {
                normal = 2.0 / 2.0;
            }
         } else if (attr == 10){
              normal = (1.0/100000.0) *Double.parseDouble(value);
         }
          else if (attr == 11){
              normal = (1.0/100000.0) * Double.parseDouble(value);
         }
         else if (attr == 12){
              normal = (1.0/168.0) * Double.parseDouble(value);
         }
         else if (attr == 13){
             if (value.equalsIgnoreCase("?")) {
                normal = 0;
            } else if (value.equalsIgnoreCase("United-States")) {
                normal = 1.0 / 41.0;
            } else if (value.equalsIgnoreCase("Cambodia")) {
                normal = 2.0 / 41.0;
            } else if (value.equalsIgnoreCase("England")) {
                normal = 3.0 / 41.0;
            } else if (value.equalsIgnoreCase("Puerto-Rico")) {
                normal = 4.0 / 41.0;
            } else if (value.equalsIgnoreCase("Canada")) {
                normal = 5.0 / 41.0;
            } else if (value.equalsIgnoreCase("Germany")) {
                normal = 6.0 / 41.0;
            } else if (value.equalsIgnoreCase("Outlying-US(Guam-USVI-etc)")) {
                normal = 7.0 / 41.0;
            } else if (value.equalsIgnoreCase("India")) {
                normal = 8.0 / 41.0;
            } else if (value.equalsIgnoreCase("Japan")) {
                normal = 9.0 / 41.0;
            } else if (value.equalsIgnoreCase("Greece")) {
                normal = 10.0 / 41.0;
            } else if (value.equalsIgnoreCase("South")) {
                normal = 11.0 / 41.0;
            } else if (value.equalsIgnoreCase("China")) {
                normal = 12.0 / 41.0;
            } else if (value.equalsIgnoreCase("Cuba")) {
                normal = 13.0 / 41.0;
            } else if (value.equalsIgnoreCase("Iran")) {
                normal = 14.0 / 41.0;
            } else if (value.equalsIgnoreCase("Honduras")) {
                normal = 15.0 / 41.0;
            } else if (value.equalsIgnoreCase("Philippines")) {
                normal = 16.0 / 41.0;
            } else if (value.equalsIgnoreCase("Italy")) {
                normal = 17.0 / 41.0;
            } else if (value.equalsIgnoreCase("Poland")) {
                normal = 18.0 / 41.0;
            } else if (value.equalsIgnoreCase("Jamaica")) {
                normal = 19.0 / 41.0;
            } else if (value.equalsIgnoreCase("Vietnam")) {
                normal = 20.0 / 41.0;
            } else if (value.equalsIgnoreCase("Mexico")) {
                normal = 21.0 / 41.0;
            } else if (value.equalsIgnoreCase("Portugal")) {
                normal = 22.0 / 41.0;
            } else if (value.equalsIgnoreCase("Ireland")) {
                normal = 23.0 / 41.0;
            } else if (value.equalsIgnoreCase("France")) {
                normal = 24.0 / 41.0;
            } else if (value.equalsIgnoreCase("Dominican-Republic")) {
                normal = 25.0 / 41.0;
            }else if (value.equalsIgnoreCase("Laos")) {
                normal = 26.0 / 41.0;
            } else if (value.equalsIgnoreCase("Ecuador")) {
                normal = 27.0 / 41.0;
            } else if (value.equalsIgnoreCase("Taiwan")) {
                normal = 28.0 / 41.0;
            } else if (value.equalsIgnoreCase("Haiti")) {
                normal = 29.0 / 41.0;
            } else if (value.equalsIgnoreCase("Columbia")) {
                normal = 30.0 / 41.0;
            } else if (value.equalsIgnoreCase("Hungary")) {
                normal = 31.0 / 41.0;
            } else if (value.equalsIgnoreCase("Guatemala")) {
                normal = 32.0 / 41.0;
            } else if (value.equalsIgnoreCase("Nicaragua")) {
                normal = 33.0 / 41.0;
            } else if (value.equalsIgnoreCase("Scotland")) {
                normal = 34.0 / 41.0;
            } else if (value.equalsIgnoreCase("Thailand")) {
                normal = 35.0 / 41.0;
            } else if (value.equalsIgnoreCase("Yugoslavia")) {
                normal = 36.0 / 41.0;
            } else if (value.equalsIgnoreCase("El-Salvador")) {
                normal = 37.0 / 41.0;
            } else if (value.equalsIgnoreCase("Trinadad&Tobago")) {
                normal = 38.0 / 41.0;
            } else if (value.equalsIgnoreCase("Peru")) {
                normal = 39.0 / 41.0;
            } else if (value.equalsIgnoreCase("Hong")) {
                normal = 40.0 / 41.0;
            } else if (value.equalsIgnoreCase("Holand-Netherlands")) {
                normal = 41.0 / 41.0;
            }
        }

        return normal;
    }

    public double[] getSolutions() {
        return fileSolutions;
    }

    public double[][] getInputs() {
        return input;
    }
}
