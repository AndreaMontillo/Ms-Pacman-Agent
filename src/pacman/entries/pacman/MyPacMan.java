package pacman.entries.pacman;

import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;
import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE>  implements Cloneable
{
	private MOVE myMove=MOVE.NEUTRAL;
	private static final int MIN_DISTANCE=20;	//if a ghost is this close, run away
        public double[] dataValue;

        
        //{-4.429475984557097,-0.7153117203153647,7.178884105800941,0.8329854215267805,-4.317279855770572,0.7203146559888434,-6.240340103161296,2.312294675069224,5.2788476344529345,2.7254221919948947,-8.657371285277241,3.2416875485634193,-6.638358970885002,1.1332452386701002,3.688438061429233,-2.389725589353027,5.901945159084612,-0.44172857405740285,-3.466517308285491,4.423156647202237,-1.9395506375080886,3.327775334666237,-6.810297283314298,-2.4787943286000917,4.284492098332441,-0.5118093860989257,1.7543112613925205,-3.385654744467292,-1.8294073859018325,-0.3797979731151244,0.33754343769309525,6.423665524320818}
        //9,5k {5.827816249301119,0.023190803323056564,8.149603014599041,0.34440028634278397,3.510711951815324,-4.584923518659951,-6.672965188296377,-2.937303668178188,-6.119890845771781,8.111790345564005,-3.7650007550646345,-0.49798688723592477,6.574711005486138,9.325479588475687,-1.5266327402291768,9.578760201725016,-3.6982357133211723,1.0053157583452785,-3.410578936910606,1.9050782075443082,-2.9242918347071454,-4.6324549605095715,-7.829563219314796,-4.780493462549462,8.996352141356068,-5.691916551637512,-1.609632999326803,-1.6570027202316844,-7.636041240598514,-3.590360802827436,-1.9754059202699503,1.5584656430039558};

        //10,3k {-9.569365494028144,5.294982532503634,8.57663249909372,1.795256711325206,-5.0019604805100935,5.926958475335158,-4.912703320083611,-2.274078407888813,-1.0490718739514975,-0.08012256324682987,-1.0688507599503352,-5.298594925695939,0.08352225794948742,-2.107038834703812,1.8568278511430338,-3.842737776799262,-2.115496473944086,8.002241176633104,3.028195203174686,7.351105267428495,-2.542593604398945,-3.4329435873804957,-0.8723652910570496,-2.8847596261280857,-6.543222217399472,-8.663941431768293,6.7428024493760725,-5.499985160505407,-6.750534343214568,-7.305992432877031,-5.365787706226074,-7.266143174347353}	
        //12,32k {1.908398576421237,7.514640866848557,-2.322274159083853,-2.26585269499531,-11.523766427772852,13.086068204032737,-11.066214624854352,-2.216783870513404,3.7644407177166457,3.140192077193584,-7.116958126796758,-0.8413221121819567,-2.6792136676111955,-9.632917952788079,0.22317012011123083,-11.32799408133476,-8.360007952248402,13.796025622137376,0.15475174265703084,13.270888056144074,-7.371208323224374,-1.4445205129338947,-10.226958088251052,-2.857399664265712,-14.99099578865414,-0.38211386504648814,4.853559279659924,-9.778813232969203,-4.0099090085293625,-6.365664441320298,-7.963860294008342,-13.599122615497649};
        //{};
        //{};
        //{};
        //{};
        //{};
	           
	
        //[0-10] --- [5-20] --- [2-23]
        
        public MyPacMan(){ 
            super();
            this.dataValue = new double[8];
            double[] temp = {9.810226675960585,1.8591762315114024,-8.312263420079965,-14.082722074812104,-12.639813142746096,-6.2336033473780965,-9.597832112967808,-13.736051894228718};
            for(int i=0; i<temp.length; i++){
               this.dataValue[i] = temp[i];  
            }
            
        }
        
        public MyPacMan(List<double[]> dimensionIn){
            super();

            Random random = new Random();
            
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
        public MOVE getMove(Game game, long timeDue) 
	{
            
                //Strategy 1: if any non-edible ghost is too close (less than MIN_DISTANCE), run away
		for(Constants.GHOST ghost : Constants.GHOST.values())
			if(game.getGhostEdibleTime(ghost)==0 && game.getGhostLairTime(ghost)==0)
				if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost))<MIN_DISTANCE)
					return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost),Constants.DM.PATH);
		
		//Strategy 2: find the nearest edible ghost and go after them 
		int minDistance=Integer.MAX_VALUE;
		Constants.GHOST minGhost=null;		
		
		for(Constants.GHOST ghost : Constants.GHOST.values())
			if(game.getGhostEdibleTime(ghost)>0)
			{
				int distance=game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost));
				
				if(distance<minDistance)
				{
					minDistance=distance;
					minGhost=ghost;
				}
			}
		
		if(minGhost!=null)	//we found an edible ghost
			return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(minGhost),Constants.DM.PATH);
		
            //Place your game logic here to play the game as Ms Pac-Man            
            double w1 = this.dataValue[0];
            double w2 = this.dataValue[1];
            double w3 = this.dataValue[2];
            double w4 = this.dataValue[3];
            double w5 = this.dataValue[4];
            double w6 = this.dataValue[5];
            double w7 = this.dataValue[6];
            double w8 = this.dataValue[7];
            double w9 = this.dataValue[8];
            double w10 = this.dataValue[9];
            double w11 = this.dataValue[10];
            double w12 = this.dataValue[11];
            double w13 = this.dataValue[12];
            double w14 = this.dataValue[13];
//            double w15 = this.dataValue[14];
//            double w16 = this.dataValue[15];
//            double w17 = this.dataValue[16];
//            double w18 = this.dataValue[17];
//            double w19 = this.dataValue[18];
//            double w20 = this.dataValue[19];
//            double w21 = this.dataValue[20];
//            double w22 = this.dataValue[21];
//            double w23 = this.dataValue[22];
//            double w24 = this.dataValue[23];
//            double w25 = this.dataValue[24];
//            double w26 = this.dataValue[25];
//            double w27 = this.dataValue[26];
//            double w28 = this.dataValue[27];
//            double w29 = this.dataValue[28];
//            double w30 = this.dataValue[29];
//            double w31 = this.dataValue[30];
//            double w32 = this.dataValue[31];
            
            
            
            // w1, w2, w3 ...
            
            int indice_pillola = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), Constants.DM.EUCLID);
            double d_pill_x = game.getNodeXCood(game.getPacmanCurrentNodeIndex()) - game.getNodeXCood(indice_pillola);
            double d_pill_y = game.getNodeYCood(game.getPacmanCurrentNodeIndex()) - game.getNodeYCood(indice_pillola);
            
            double d_ghost_edible = +Double.POSITIVE_INFINITY;
            double d_ghost_edible_x = +1000;
            double d_ghost_edible_y = +1000;
            int indice_ghost_edible = -1;
            int indice_ghost1 = game.getGhostCurrentNodeIndex(Constants.GHOST.values()[0]);
            int indice_ghost2 = game.getGhostCurrentNodeIndex(Constants.GHOST.values()[1]);;
            int indice_ghost3 = game.getGhostCurrentNodeIndex(Constants.GHOST.values()[2]);;
            int indice_ghost4 = game.getGhostCurrentNodeIndex(Constants.GHOST.values()[3]);;

            double currDistance;
            for(Constants.GHOST ghost : Constants.GHOST.values()){      
                currDistance = game.getEuclideanDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost));
                if(game.getGhostEdibleTime(ghost)==0 && currDistance < d_ghost_edible){
                    indice_ghost_edible = game.getGhostCurrentNodeIndex(ghost);
                    d_ghost_edible = currDistance;
                }
            }
            double d_ghost1_x = game.getNodeXCood(game.getPacmanCurrentNodeIndex()) - game.getNodeXCood(indice_ghost1);
            double d_ghost1_y = game.getNodeYCood(game.getPacmanCurrentNodeIndex()) - game.getNodeYCood(indice_ghost1);
            
            double d_ghost2_x = game.getNodeXCood(game.getPacmanCurrentNodeIndex()) - game.getNodeXCood(indice_ghost2);
            double d_ghost2_y = game.getNodeYCood(game.getPacmanCurrentNodeIndex()) - game.getNodeYCood(indice_ghost2);
            double d_ghost3_x = game.getNodeXCood(game.getPacmanCurrentNodeIndex()) - game.getNodeXCood(indice_ghost3);
            double d_ghost3_y = game.getNodeYCood(game.getPacmanCurrentNodeIndex()) - game.getNodeYCood(indice_ghost3);
            double d_ghost4_x = game.getNodeXCood(game.getPacmanCurrentNodeIndex()) - game.getNodeXCood(indice_ghost4);
            double d_ghost4_y = game.getNodeYCood(game.getPacmanCurrentNodeIndex()) - game.getNodeYCood(indice_ghost4);
            
            if (d_ghost_edible > -1){
                d_ghost_edible_x = game.getNodeXCood(game.getPacmanCurrentNodeIndex()) - game.getNodeXCood(indice_ghost_edible);
                d_ghost_edible_y = game.getNodeYCood(game.getPacmanCurrentNodeIndex()) - game.getNodeYCood(indice_ghost_edible);
            }
            
            double d_power_x = 1000;
            double d_power_y = 1000;
            int indice_power = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), Constants.DM.EUCLID);
            if (indice_power>1){
                d_power_x = game.getNodeXCood(game.getPacmanCurrentNodeIndex()) - game.getNodeXCood(indice_power);
                d_power_y = game.getNodeYCood(game.getPacmanCurrentNodeIndex()) - game.getNodeYCood(indice_power);
            }
            
            //-4.429475984557097,-0.7153117203153647,7.178884105800941,0.8329854215267805,-4.317279855770572,0.7203146559888434,-6.240340103161296,2.312294675069224,5.2788476344529345,2.7254221919948947,-8.657371285277241,3.2416875485634193,-6.638358970885002,1.1332452386701002,3.688438061429233,-2.389725589353027,5.901945159084612,-0.44172857405740285,-3.466517308285491,4.423156647202237,-1.9395506375080886,3.327775334666237,-6.810297283314298,-2.4787943286000917,4.284492098332441,-0.5118093860989257,1.7543112613925205,-3.385654744467292,-1.8294073859018325,-0.3797979731151244,0.33754343769309525,6.423665524320818
            //-9.569365494028144,5.294982532503634,8.57663249909372,1.795256711325206,-5.0019604805100935,5.926958475335158,-4.912703320083611,-2.274078407888813,-1.0490718739514975,-0.08012256324682987,-1.0688507599503352,-5.298594925695939,0.08352225794948742,-2.107038834703812,1.8568278511430338,-3.842737776799262,-2.115496473944086,8.002241176633104,3.028195203174686,7.351105267428495,-2.542593604398945,-3.4329435873804957,-0.8723652910570496,-2.8847596261280857,-6.543222217399472,-8.663941431768293,6.7428024493760725,-5.499985160505407,-6.750534343214568,-7.305992432877031,-5.365787706226074,-7.266143174347353
//            double left = w1*d_pill_x + w2*d_ghost_x+ w3*d_power_x + w4*d_ghost_edible_x + w17*d_pill_y + w18*d_ghost_y+ w19*d_power_y + w20*d_ghost_edible_y;
//            double right = w5*d_pill_x + w6*d_ghost_x + w7*d_power_x + w8*d_ghost_edible_x + w21*d_pill_y + w22*d_ghost_y+ w23*d_power_y + w24*d_ghost_edible_y;
//            double up = w9*d_pill_x + w10*d_ghost_x + w11*d_power_x + w12*d_ghost_edible_x + w25*d_pill_y + w26*d_ghost_y+ w27*d_power_y + w28*d_ghost_edible_y;
//            double down = w13*d_pill_x + w14*d_ghost_x + w15*d_power_x + w16*d_ghost_edible_x + w29*d_pill_y + w30*d_ghost_y+ w31*d_power_y + w32*d_ghost_edible_y;
            
//            double max = Math.max(Math.max(left, right), Math.max(down, up));
//            if (max == left) return MOVE.LEFT;
//            if (max == right) return MOVE.RIGHT;
//            if (max == down) return MOVE.DOWN;
//            if (max == up) return MOVE.UP;
            
            double direction = w1*d_pill_x + w2*d_ghost1_x + w3*d_power_x + w4*d_ghost_edible_x + w5*d_pill_y + w6*d_ghost1_y+ w7*d_power_y + w8*d_ghost_edible_y + w9*d_ghost2_x+ w10*d_ghost2_y+ w11*d_ghost3_x+ w12*d_ghost3_y+ w13*d_ghost4_x + w14*d_ghost4_y;
            
            if (direction<-10)
                direction = -10;
            else if(direction>10)
                direction =10;
            
            MOVE[] legal_moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
            
          
            
            if (direction >= -5 && direction <0) return MOVE.DOWN;
            if (direction >= 0 && direction < 5) return MOVE.RIGHT;
            if (direction >= 5 && direction <=10) return MOVE.UP;
                   
                    
            System.out.println("MAIIIIIIII, cit.");
            return MOVE.NEUTRAL;
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