/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman;

/**
 *
 * @author USER
 */
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


public class MiniMax_vecchio {
	private final int startDepth = 50;
	private long availableTime;

	public MiniMax_vecchio(long availableTime) {
            this.availableTime = availableTime;
        }
	
	public MOVE getAction(Game g) throws Exception {
                int depth;
		long startTime = System.currentTimeMillis();
		long endTime = startTime + availableTime;
		
		Game sim = g.copy();
                MOVE[] array = sim.getPossibleMoves(sim.getPacmanCurrentNodeIndex());
                List <MOVE> legalMoves = Arrays.asList(array);
                
		MOVE lastBest = null;
		for (depth = startDepth; System.currentTimeMillis() < endTime; depth++) { 
			MOVE bestMove = null;
			double bestScore = 0;
                        double score = 0;
			for (MOVE move : legalMoves) {
                                EnumMap<GHOST, MOVE> map = new EnumMap<>(GHOST.class);
                                map.put(GHOST.values()[0], MOVE.NEUTRAL);
                                map.put(GHOST.values()[1], MOVE.NEUTRAL);
                                map.put(GHOST.values()[2], MOVE.NEUTRAL);
                                map.put(GHOST.values()[3], MOVE.NEUTRAL);
                                Game next = sim.copy(); 
                                next.advanceGame(move, map);
                           
				
				score = getMinScoreAlphaBeta(next, 0, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, endTime);
				if (System.currentTimeMillis() > endTime) {
					break;
				}
				
				if (bestMove == null || score > bestScore) {
					bestMove = move;
					bestScore = score;
				}
			}
			if (System.currentTimeMillis() < endTime) {
				lastBest = bestMove;
			} 
		}	
                
//                if (System.currentTimeMillis() > endTime) {
//                    System.out.println(depth);
//                }
                
		MOVE direction = null;
		if(lastBest!=null){
			direction = lastBest;
		}
		
		return direction;		
		
	}
	
	/*
	 * Consider all possible moves by the ghosts
	 */
	private double getMinScoreAlphaBeta(Game s, int gNum, int depth, double alpha, double beta, long endTime) throws Exception {	
		double res;
                double score;
                res = computeUtility(s);
		if (depth == 0 || s.wasPacManEaten()) {
			return res;
		} else {
			//res = Double.POSITIVE_INFINITY;
                        score = res;
                        GHOST g = GHOST.values()[gNum];
			MOVE[] array = s.getPossibleMoves(s.getGhostCurrentNodeIndex(g),s.getGhostLastMoveMade(g));
                        List <MOVE> legalMoves = Arrays.asList(array);
			for(MOVE move : legalMoves){
				if (System.currentTimeMillis() > endTime) {
					return 0;
				}
                                EnumMap<GHOST, MOVE> map = new EnumMap<>(GHOST.class);
                                map.put(g, move);
                                
                                Game next = s.copy();
                                next.advanceGame(MOVE.NEUTRAL, map);
				
				if(gNum == GHOST.values().length-1){
					score += getMaxScoreAlphaBeta(next, depth - 1, alpha, beta, endTime);
				}else{
					score += getMinScoreAlphaBeta(next, gNum+1, depth, alpha, beta, endTime);
				}
				
				res = Math.min(res, score);
				beta = Math.min(beta, res);
				if (beta <= alpha) {
					break;
				}
			}  		
		}
		return res;
	}
	
	/*
	 * Consider all possible moves by pacman
	 */
	private double getMaxScoreAlphaBeta(Game s, int depth, double alpha, double beta, long endTime) throws Exception {
		double res;
                double score;
                res = computeUtility(s);
		if (depth == 0  || s.wasPacManEaten()) {
			return res;
		} else {
                        
			MOVE[] array = s.getPossibleMoves(s.getPacmanCurrentNodeIndex(), s.getPacmanLastMoveMade());
                        List <MOVE> legalMoves = Arrays.asList(array);
                        //res = Double.NEGATIVE_INFINITY;
                        score = res;
                        
                        for (MOVE move : legalMoves) {
				if (System.currentTimeMillis() > endTime) {
					return 0;
				}
                                
                                EnumMap<GHOST, MOVE> map = new EnumMap<>(GHOST.class);
                                map.put(GHOST.values()[0], MOVE.NEUTRAL);
                                map.put(GHOST.values()[1], MOVE.NEUTRAL);
                                map.put(GHOST.values()[2], MOVE.NEUTRAL);
                                map.put(GHOST.values()[3], MOVE.NEUTRAL);
                                Game next = s.copy();
                                next.advanceGame(move, map);
                                
                                score += getMin2ScoreAlphaBeta(next, 0,  depth - 1, alpha, beta, endTime);
				res = Math.max(res, score);
				alpha = Math.max(alpha, res);
				if (beta <= alpha) {
					break;
				}
			}
		}
		return res;
	}
        
        
        private static double computeUtility(Game game){
            
            double distance = Double.POSITIVE_INFINITY;
            double currDistance;
            GHOST nearestGhost = null;
            double utility = game.getScore();

            for(GHOST ghost : GHOST.values()){      
                currDistance = game.getDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost), Constants.DM.EUCLID);
                if(currDistance < distance){
                    distance = currDistance;
                    nearestGhost = ghost;
                }
            }
                        
            if(game.getGhostEdibleTime(nearestGhost)==0){
                if (distance < 25)
                    utility += distance*5;
            }else{
                utility -= distance*4;
            }

            int index_pillola = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), Constants.DM.EUCLID);
            double dist_pillola = game.getDistance(game.getPacmanCurrentNodeIndex(), index_pillola, Constants.DM.EUCLID);
            if (dist_pillola < 3.0d) dist_pillola = 3.0d;
            utility -= dist_pillola;
            

            if(game.wasPacManEaten()){
                    utility -= 500;
            }
            
            if(game.wasPacManEaten() && game.getPacmanNumberOfLivesRemaining() == 0){
                    utility -= 15000;
            }
            
            return utility;
        }
        
        
        /*
	 * Consider all possible moves by the ghosts
	 */
	private double getMin2ScoreAlphaBeta(Game s, int gNum, int depth, double alpha, double beta, long endTime) throws Exception {	
		double res;
                double score;
                res = computeUtility(s);
		if (depth == 0 || s.wasPacManEaten()) {
			return res;
		} else {
			//res = Double.POSITIVE_INFINITY;
                        score = res;
                        double distance = Double.POSITIVE_INFINITY;
                        double currDistance;
                        GHOST nearestGhost = null;
                        for(GHOST ghost : GHOST.values()){      
                            currDistance = s.getDistance(s.getPacmanCurrentNodeIndex(),s.getGhostCurrentNodeIndex(ghost), Constants.DM.EUCLID);
                            if(currDistance < distance){
                                distance = currDistance;
                                nearestGhost = ghost;
                            }
                        }
                        
			MOVE[] array = s.getPossibleMoves(s.getGhostCurrentNodeIndex(nearestGhost),s.getGhostLastMoveMade(nearestGhost));
                        
                        List <MOVE> legalMoves = Arrays.asList(array);
			for(MOVE move : legalMoves){
				if (System.currentTimeMillis() > endTime) {
					return 0;
				}
                                EnumMap<GHOST, MOVE> map = new EnumMap<>(GHOST.class);
                                map.put(nearestGhost, move);
                                
                                Game next = s.copy();
                                next.advanceGame(MOVE.NEUTRAL, map);
				
                                score += getMaxScoreAlphaBeta(next, depth - 1, alpha, beta, endTime);
				
				res = Math.min(res, score);
				beta = Math.min(beta, res);
				if (beta <= alpha) {
					break;
				}
			}  		
		}
		return res;
	}
        
        
}
