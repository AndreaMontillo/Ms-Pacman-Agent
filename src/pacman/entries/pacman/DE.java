/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;
import pacman.Executor;
import pacman.controllers.examples.AggressiveGhosts;
import pacman.controllers.examples.StarterGhosts;

public class DE {
    
    // list of generated populations
    List<MyPacMan> population;
    
    // populiation size to generate at the begining 
    int POPULATION_SIZE = 80;
    
    // crossover probability [0,1]
    double CROSSOVER_PROBABILITY = 0.4;
    
    // differential weight [0,2]
    double DIFFERENTIAL_WEIGHT = 0.8d; // F che sta sulle slides

    // number of iteration
    int ITERATION_NO = 100;
    
    // random number generator
    Random random;
    
    PrintWriter pw;  
    
    public static void main(String[] args){
        
        double b = 15.0d;
        // define lower/ upper bounds for each required dimensions
        double[] dimension1Bounds = new double[2]; dimension1Bounds[0] = -b; dimension1Bounds[1] = b;
        double[] dimension2Bounds = new double[2]; dimension2Bounds[0] = -b; dimension2Bounds[1] = b;
        double[] dimension3Bounds = new double[2]; dimension3Bounds[0] = -b; dimension3Bounds[1] = b;
        double[] dimension4Bounds = new double[2]; dimension4Bounds[0] = -b; dimension4Bounds[1] = b;
        double[] dimension5Bounds = new double[2]; dimension5Bounds[0] = -b; dimension5Bounds[1] = b;
        double[] dimension6Bounds = new double[2]; dimension6Bounds[0] = -b; dimension6Bounds[1] = b;
        double[] dimension7Bounds = new double[2]; dimension7Bounds[0] = -b; dimension7Bounds[1] = b;
        double[] dimension8Bounds = new double[2]; dimension8Bounds[0] = -b; dimension8Bounds[1] = b;
        double[] dimension9Bounds = new double[2]; dimension9Bounds[0] = -b; dimension9Bounds[1] = b;
        double[] dimension10Bounds = new double[2]; dimension10Bounds[0] = -b; dimension10Bounds[1] = b;
        double[] dimension11Bounds = new double[2]; dimension11Bounds[0] = -b; dimension11Bounds[1] = b;
        double[] dimension12Bounds = new double[2]; dimension12Bounds[0] = -b; dimension12Bounds[1] = b;
        double[] dimension13Bounds = new double[2]; dimension13Bounds[0] = -b; dimension13Bounds[1] = b;
        double[] dimension14Bounds = new double[2]; dimension14Bounds[0] = -b; dimension14Bounds[1] = b;
//        double[] dimension15Bounds = new double[2]; dimension15Bounds[0] = -15.0d; dimension15Bounds[1] = 15.0d;
//        double[] dimension16Bounds = new double[2]; dimension16Bounds[0] = -15.0d; dimension16Bounds[1] = 15.0d;
        
//        double[] dimension17Bounds = new double[2]; dimension17Bounds[0] = -b; dimension17Bounds[1] = b;
//        double[] dimension18Bounds = new double[2]; dimension18Bounds[0] = -b; dimension18Bounds[1] = b;
//        double[] dimension19Bounds = new double[2]; dimension19Bounds[0] = -b; dimension19Bounds[1] = b;
//        double[] dimension20Bounds = new double[2]; dimension20Bounds[0] = -b; dimension20Bounds[1] = b;
//        double[] dimension21Bounds = new double[2]; dimension21Bounds[0] = -15.0d; dimension21Bounds[1] = 15.0d;
//        double[] dimension22Bounds = new double[2]; dimension22Bounds[0] = -15.0d; dimension22Bounds[1] = 15.0d;
//        double[] dimension23Bounds = new double[2]; dimension23Bounds[0] = -15.0d; dimension23Bounds[1] = 15.0d;
//        double[] dimension24Bounds = new double[2]; dimension24Bounds[0] = -15.0d; dimension24Bounds[1] = 15.0d;
//        double[] dimension25Bounds = new double[2]; dimension25Bounds[0] = -15.0d; dimension25Bounds[1] = 15.0d;
//        double[] dimension26Bounds = new double[2]; dimension26Bounds[0] = -15.0d; dimension26Bounds[1] = 15.0d;
//        double[] dimension27Bounds = new double[2]; dimension27Bounds[0] = -15.0d; dimension27Bounds[1] = 15.0d;
//        double[] dimension28Bounds = new double[2]; dimension28Bounds[0] = -15.0d; dimension28Bounds[1] = 15.0d;
//        double[] dimension29Bounds = new double[2]; dimension29Bounds[0] = -15.0d; dimension29Bounds[1] = 15.0d;
//        double[] dimension30Bounds = new double[2]; dimension30Bounds[0] = -15.0d; dimension30Bounds[1] = 15.0d;
//        double[] dimension31Bounds = new double[2]; dimension31Bounds[0] = -15.0d; dimension31Bounds[1] = 15.0d;
//        double[] dimension32Bounds = new double[2]; dimension32Bounds[0] = -15.0d; dimension32Bounds[1] = 15.0d;
        
        // add all dimension to a list, and this will be passed to DE
        List<double[]> dimensionList = new LinkedList<>();
        
        dimensionList.add(dimension1Bounds);
        dimensionList.add(dimension2Bounds);
        dimensionList.add(dimension3Bounds);
        dimensionList.add(dimension4Bounds);
        dimensionList.add(dimension5Bounds);
        dimensionList.add(dimension6Bounds);
        dimensionList.add(dimension7Bounds);
        dimensionList.add(dimension8Bounds);
        dimensionList.add(dimension9Bounds);
        dimensionList.add(dimension10Bounds);
        dimensionList.add(dimension11Bounds);
        dimensionList.add(dimension12Bounds);
        dimensionList.add(dimension13Bounds);
        dimensionList.add(dimension14Bounds);
//        dimensionList.add(dimension15Bounds);
//        dimensionList.add(dimension16Bounds);
        
//        dimensionList.add(dimension17Bounds);
//        dimensionList.add(dimension18Bounds);
//        dimensionList.add(dimension19Bounds);
//        dimensionList.add(dimension20Bounds);
//        dimensionList.add(dimension21Bounds);
//        dimensionList.add(dimension22Bounds);
//        dimensionList.add(dimension23Bounds);
//        dimensionList.add(dimension24Bounds);
//        dimensionList.add(dimension25Bounds);
//        dimensionList.add(dimension26Bounds);
//        dimensionList.add(dimension27Bounds);
//        dimensionList.add(dimension28Bounds);
//        dimensionList.add(dimension29Bounds);
//        dimensionList.add(dimension30Bounds);
//        dimensionList.add(dimension31Bounds);
//        dimensionList.add(dimension32Bounds);
        
        System.out.println("INIZIO ADDESTRAMENTO");
        
        DE de = new DE();
        
        // start optimising process and return the best candiate after number of spcecified iteration
        System.out.println("Best combination found: " + de.optimise(dimensionList));             
    }
    
    // fitness function
    public double fitFunction(MyPacMan aCandidate){
       
        Executor exec=new Executor();
        
        int iterazioni = 10;
        double media_score = exec.runExperiment(aCandidate,new AggressiveGhosts(),iterazioni);

        return media_score;
        
        
        // Rastrigin function
        //double value = 10.0d * aCandidate.dataValue.length;
        
        //for(int i = 0; i < aCandidate.dataValue.length; i++){
        //    value = value + Math.pow(aCandidate.dataValue[i], 2.0) - 10.0 * Math.cos(2 * Math.PI * aCandidate.dataValue[i]); 
        //}
        
//      //  double[] values = aCandidate.dataValue; 
//      //  double value = 100 * Math.pow(Math.pow(values[0], 2) - values[1], 2) + Math.pow(1 - values[0], 2);
        
        //return value;
    }
        
    // DE constructor
    public DE(){ 
        random = new Random();
        population = new LinkedList<>();
    }
    
    public MyPacMan optimise(List<double[]> dimensionList){
         
        // generate population up to the define limit
        for(int i = 0; i < POPULATION_SIZE; i++){
            MyPacMan individual = new MyPacMan(dimensionList);
            population.add(individual);
            
        }       
                
       // try more than one iteration 
       for(int iterationCount = 0; iterationCount < ITERATION_NO; iterationCount++){
           
           System.out.println("Iterazione n°: " + Integer.toString(iterationCount));
           
           if(iterationCount%10 == 0 || iterationCount == ITERATION_NO-1){
            try {
                pw = new PrintWriter(new File("data/popoluation_" + Integer.toString(iterationCount) +".csv"));
            } catch (FileNotFoundException ex) {
                
            }    
            
            
            for(int n = 0; n < dimensionList.size(); n++){      
                pw.write("v" + Integer.toString(n));
                pw.write(",");
            }
        
            pw.write("fValue");

            pw.write("\n");
        
            for (MyPacMan individual : population) {
                pw.write(individual.toString());
                pw.write(",");
                pw.write(Double.toString(fitFunction(individual)));
                pw.write("\n");
            }
            
            pw.flush();
           }
           
            int loop = 0;
        
            // main loop for evolution
            while(loop < population.size()){       

            MyPacMan original = null;
            MyPacMan candidate = null;
            boolean boundsHappy;

            do{
                boundsHappy = true;
                // pick an agent from the the population
                int x = loop;
                int a,b,c = -1;

                // pick three random agents from the population
                // make sure that they are not identical to selected agent from
                // the population 

                do{
                    a = random.nextInt(population.size());
                }while(x == a);
                do{
                    b = random.nextInt(population.size());
                }while(b==x || b==a);
                do{
                    c = random.nextInt(population.size());
                }while(c == x || c == a || c == b);

                // create three agent individuals
                MyPacMan individual1 = population.get(a);
                MyPacMan individual2 = population.get(b);
                MyPacMan individual3 = population.get(c);

                // create a noisy random candidate
                MyPacMan noisyRandomCandicate = new MyPacMan(dimensionList);

                // mutation process
                // if an element of the trial parameter vector is
                // found to violate the bounds after mutation and crossover, it is reset in such a way that the bounds
                // are respected (with the specific protocol depending on the implementation)
                for(int n = 0; n < dimensionList.size(); n++){     
                    noisyRandomCandicate.dataValue[n] = (individual1.dataValue[n] + DIFFERENTIAL_WEIGHT * (individual2.dataValue[n] - individual3.dataValue[n]));               
                }           

                // Create a trial candicate 
                original = population.get(x);
                candidate = new MyPacMan(dimensionList);

                // copy values from original agent to the candidate agent
                for(int n = 0; n < dimensionList.size(); n++){             
                    candidate.dataValue[n] = original.dataValue[n];
                }  

                // crossver process with the selected individual
                // pick a random dimension, which defintely takes the value from the noisy random candidate
                int R = random.nextInt(dimensionList.size());

                for(int n = 0; n < dimensionList.size(); n++){

                    double crossoverProbability = random.nextDouble();

                    if(crossoverProbability < CROSSOVER_PROBABILITY || n == R){
                        candidate.dataValue[n] = noisyRandomCandicate.dataValue[n];
                    }

                }

                // check here if the trial candiate satisfies bounds for each value
                for(int n = 0; n < dimensionList.size(); n++){ 
                    if(candidate.dataValue[n] < dimensionList.get(n)[0] || candidate.dataValue[n] > dimensionList.get(n)[1]){
                       boundsHappy = false;
                    }
                }

            }while(boundsHappy == false);

                //see if the candidate is better than original, if so replace it
                if(fitFunction(original) < fitFunction(candidate)){
                        population.remove(original);
                        population.add(candidate);     
                }
                loop++;
            }        
        }
        
       MyPacMan bestFitness = new MyPacMan(dimensionList);
   
       // selecting the final best agent from the the population
       for(int i = 0; i < population.size(); i++){
           MyPacMan individual = population.get(i);
           
            if(fitFunction(bestFitness) < fitFunction(individual)){
                
               try {
                   bestFitness = (MyPacMan) individual.clone();
               } catch (CloneNotSupportedException ex) {
                  
               }
            }
       }
       
       System.out.println("Fitness migliore: " + fitFunction(bestFitness));
       return bestFitness;
    }
            
    public class Individual implements Cloneable{
      
        // each element a value from valid range for a given dimension
        double[] dataValue;
        
        public Individual(List<double[]> dimensionIn){
            int noDimension = dimensionIn.size();       
            // initialse data vector
            dataValue = new double[noDimension];
                 
            // for each dimension, create corresponding data point between given range
            for(int dimensionIndex = 0; dimensionIndex < noDimension; dimensionIndex++){
                
                double dimensionLowerBound = dimensionIn.get(dimensionIndex)[0];
                double dimensionUpperBound = dimensionIn.get(dimensionIndex)[1];        
                
                DoubleStream valueGenerator = random.doubles(dimensionLowerBound, dimensionUpperBound);
                
                dataValue[dimensionIndex] = valueGenerator.iterator().nextDouble();
            }
        }
        @Override
        public String toString(){
            
            String string = "";
            
            for (int i = 0; i < dataValue.length; i++) {
                string += Double.toString(dataValue[i]);
                
                if((i + 1) != dataValue.length){
                    string += ",";
                }
            }
            
            return string;
        }
        
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    

}