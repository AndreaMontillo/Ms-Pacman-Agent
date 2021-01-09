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

public class MiniMax {

    private final int startDepth = 80;
    private long availableTime;
    private int func;

    public MiniMax(long availableTime, int func) {
        this.availableTime = availableTime;
        this.func = func;
    }

    public MOVE getAction(Game g) throws Exception {
        int depth;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + availableTime;

        Game sim = g.copy();
        MOVE[] array = sim.getPossibleMoves(sim.getPacmanCurrentNodeIndex());
        List<MOVE> legalMoves = Arrays.asList(array);

        MOVE lastBest = null;
        for (depth = startDepth; System.currentTimeMillis() < endTime && depth < 1000; depth++) {
            MOVE bestMove = null;
            double bestScore = 0;
            double score = 0;
            for (MOVE move : legalMoves) {
                EnumMap<GHOST, MOVE> map = new EnumMap<>(GHOST.class);
                map.put(GHOST.values()[0], sim.getApproximateNextMoveTowardsTarget(sim.getGhostCurrentNodeIndex(GHOST.values()[0]), sim.getPacmanCurrentNodeIndex(), sim.getGhostLastMoveMade(GHOST.values()[0]), Constants.DM.PATH));
                map.put(GHOST.values()[1], sim.getApproximateNextMoveTowardsTarget(sim.getGhostCurrentNodeIndex(GHOST.values()[1]), sim.getPacmanCurrentNodeIndex(), sim.getGhostLastMoveMade(GHOST.values()[1]), Constants.DM.PATH));
                map.put(GHOST.values()[2], sim.getApproximateNextMoveTowardsTarget(sim.getGhostCurrentNodeIndex(GHOST.values()[2]), sim.getPacmanCurrentNodeIndex(), sim.getGhostLastMoveMade(GHOST.values()[2]), Constants.DM.PATH));
                map.put(GHOST.values()[3], sim.getApproximateNextMoveTowardsTarget(sim.getGhostCurrentNodeIndex(GHOST.values()[3]), sim.getPacmanCurrentNodeIndex(), sim.getGhostLastMoveMade(GHOST.values()[3]), Constants.DM.PATH));

                Game next = sim.copy();
                next.advanceGame(move, map);

                score = getMax2ScoreAlphaBeta(next, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, endTime);
                //score = getMinScoreAlphaBeta(next, 0, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, endTime);
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

//        if (System.currentTimeMillis() > endTime) {
//            System.out.println(depth);
//        }

        MOVE direction = null;
        if (lastBest != null) {
            direction = lastBest;
        }

        if (direction == null) System.out.println("AAAAAAAAAAAAAAAAAAAAAAA: "+ depth);
        
        return direction;

    }

    /*
     * Consider all possible moves by the ghosts
     */
    private double getMinScoreAlphaBeta(Game s, int gNum, int depth, double alpha, double beta, long endTime) throws Exception {
        double res;
        double score;
        if (func == 1) {
            res = computeUtility(s);
        } else if (func == 2) {
            res = computeUtility2(s);
        } else if (func == 3) {
            res = computeUtility3(s);
        } else if (func == 5) {
            res = computeUtility5(s);
        } else {
            res = s.getScore();
        }

        if (depth == 0 || s.wasPacManEaten()) {
            return res;
        } else {
            //res = Double.POSITIVE_INFINITY;
            score = res;
            GHOST g = GHOST.values()[gNum];
            MOVE[] array = s.getPossibleMoves(s.getGhostCurrentNodeIndex(g), s.getGhostLastMoveMade(g));
            List<MOVE> legalMoves = Arrays.asList(array);
            for (MOVE move : legalMoves) {
                if (System.currentTimeMillis() > endTime) {
                    return 0;
                }
                EnumMap<GHOST, MOVE> map = new EnumMap<>(GHOST.class);
                for (int k = 0; k < 4; k++) {
                    if (k == gNum) {
                        map.put(GHOST.values()[gNum], move);
                    } else {
                        map.put(GHOST.values()[k], MOVE.NEUTRAL);
                    }
                }

                Game next = s.copy();
                //next.advanceGame(MOVE.NEUTRAL, map);
                next.updateGhosts(map);
                next.updateGame();

                if (gNum == GHOST.values().length - 1) {
                    score += getMaxScoreAlphaBeta(next, depth - 1, alpha, beta, endTime);
                } else {
                    score += getMinScoreAlphaBeta(next, gNum + 1, depth, alpha, beta, endTime);
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
        if (func == 1) {
            res = computeUtility(s);
        } else if (func == 2) {
            res = computeUtility2(s);
        } else if (func == 3) {
            res = computeUtility3(s);
        } else if (func == 5) {
            res = computeUtility5(s);
        } else {
            res = s.getScore();
        }

        if (depth == 0 || s.wasPacManEaten()) {
            return res;
        } else {

            MOVE[] array = s.getPossibleMoves(s.getPacmanCurrentNodeIndex(), s.getPacmanLastMoveMade());
            List<MOVE> legalMoves = Arrays.asList(array);
            //res = Double.NEGATIVE_INFINITY;
            score = res;

            for (MOVE move : legalMoves) {
                if (System.currentTimeMillis() > endTime) {
                    return 0;
                }

//                                EnumMap<GHOST, MOVE> map = new EnumMap<>(GHOST.class);
//                                map.put(GHOST.values()[0], MOVE.NEUTRAL);
//                                map.put(GHOST.values()[1], MOVE.NEUTRAL);
//                                map.put(GHOST.values()[2], MOVE.NEUTRAL);
//                                map.put(GHOST.values()[3], MOVE.NEUTRAL);
                Game next = s.copy();
                //next.advanceGame(move, map);
                next.updatePacMan(move);
                next.updateGame();

                score += getMinScoreAlphaBeta(next, 0, depth - 1, alpha, beta, endTime);
                res = Math.max(res, score);
                alpha = Math.max(alpha, res);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return res;
    }

    private double getMax2ScoreAlphaBeta(Game s, int depth, double alpha, double beta, long endTime) throws Exception {
        double res;
        double score;

        if (func == 1) {
            res = computeUtility(s);
        } else if (func == 2) {
            res = computeUtility2(s);
        } else if (func == 3) {
            res = computeUtility3(s);
        } else if (func == 5) {
            res = computeUtility5(s);
        } else {
            res = s.getScore();
        }

        if (depth == 0 || s.wasPacManEaten()) {
            return res;
        } else {

            MOVE[] array = s.getPossibleMoves(s.getPacmanCurrentNodeIndex(), s.getPacmanLastMoveMade());
            List<MOVE> legalMoves = Arrays.asList(array);

            for (MOVE move : legalMoves) {
                if (System.currentTimeMillis() > endTime) {
                    return -1000;
                }

                Game next = s.copy();
                EnumMap<GHOST, MOVE> map = new EnumMap<>(GHOST.class);

                map.put(GHOST.values()[0], next.getApproximateNextMoveTowardsTarget(next.getGhostCurrentNodeIndex(GHOST.values()[0]), next.getPacmanCurrentNodeIndex(), next.getGhostLastMoveMade(GHOST.values()[0]), Constants.DM.PATH));
                map.put(GHOST.values()[1], next.getApproximateNextMoveTowardsTarget(next.getGhostCurrentNodeIndex(GHOST.values()[1]), next.getPacmanCurrentNodeIndex(), next.getGhostLastMoveMade(GHOST.values()[1]), Constants.DM.PATH));
                map.put(GHOST.values()[2], next.getApproximateNextMoveTowardsTarget(next.getGhostCurrentNodeIndex(GHOST.values()[2]), next.getPacmanCurrentNodeIndex(), next.getGhostLastMoveMade(GHOST.values()[2]), Constants.DM.PATH));
                map.put(GHOST.values()[3], next.getApproximateNextMoveTowardsTarget(next.getGhostCurrentNodeIndex(GHOST.values()[3]), next.getPacmanCurrentNodeIndex(), next.getGhostLastMoveMade(GHOST.values()[3]), Constants.DM.PATH));

                next.advanceGame(move, map);

                score = getMax2ScoreAlphaBeta(next, depth - 1, alpha, beta, endTime);
                res = Math.max(res, score);
            }
        }
        return res;
    }

    private static double computeUtility(Game game) {

        double distance;
        double utility = game.getScore();

//        for (GHOST ghost : GHOST.values()) {
//            currDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), Constants.DM.PATH);
//            if (currDistance < distance) {
//                distance = currDistance;
//                nearestGhost = ghost;
//            }
//        }
//        for (GHOST gh : GHOST.values()) {
//            if (game.getGhostEdibleTime(gh) == 0) {
//                if (distance < 25 && game.getGhostCurrentNodeIndex(gh) == game.getGhostCurrentNodeIndex(nearestGhost)) {
//                    utility += distance * 5;
//                } else if (distance < 15) {
//                    utility += distance * 2;
//                }
//            } else {
//                utility -= distance * 4;
//            }
//        }
        
        for (GHOST ghost : GHOST.values()){
            distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), Constants.DM.PATH);
            if (game.getGhostEdibleTime(ghost) == 0 ) {
                utility += distance * 5;
            } else {
                utility -= distance * 4;
            }
        }
        

        int index_pillola = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), Constants.DM.EUCLID);
        if (index_pillola > 0) {
            double dist_pillola = game.getDistance(game.getPacmanCurrentNodeIndex(), index_pillola, Constants.DM.PATH);
            if (dist_pillola < 3) {
                dist_pillola = 3;
            }
            utility -= dist_pillola;
        }

        if (game.wasPacManEaten()) {
            utility -= 500;
        }

        if (game.wasPacManEaten() && game.getPacmanNumberOfLivesRemaining() == 0) {
            utility -= 15000;
        }

        return utility;
    }

    private static double computeUtility2(Game game) {
        double distance = Double.POSITIVE_INFINITY;
        double currDistance;
        GHOST nearestGhost = null;
        double utility = game.getScore();

//            for(GHOST ghost : GHOST.values()){      
//                currDistance = game.getDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost), Constants.DM.EUCLID);
//                if(currDistance < distance){
//                    distance = currDistance;
//                    nearestGhost = ghost;
//                }
//            }
//                        
//            if(game.getGhostEdibleTime(nearestGhost)==0){
//                if (distance < 25)
//                    utility += distance*5;
//            }else{
//                utility -= distance*4;
//            }
        
        if (game.wasPillEaten()) utility += 20;
        if (game.wasPowerPillEaten()) utility -= 100;
        
//        int index_power_pillola = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), Constants.DM.PATH);
//        if (index_power_pillola > -1) {
//            double dist_power_pillola = game.getDistance(game.getPacmanCurrentNodeIndex(), index_power_pillola, Constants.DM.PATH);
////            if(dist_power_pillola<5){
////                utility -= dist_power_pillola*5;
////            }
//            utility -= dist_power_pillola * 20;
//        }

        if (game.wasPacManEaten()) {
            utility -= 500;
        }

        if (game.wasPacManEaten() && game.getPacmanNumberOfLivesRemaining() == 0) {
            utility -= 15000;
        }

        return utility;
    }

    private static double computeUtility3(Game game) {
        double distance = Double.POSITIVE_INFINITY;
        double currDistance;
        double utility = game.getScore();
        boolean esiste_edible_ghost = false;
        double distance_edible = Double.POSITIVE_INFINITY;
        GHOST edible_ghost = null;

        for (GHOST ghost : GHOST.values()) {
            currDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), Constants.DM.PATH);
            if (currDistance < distance) {
                distance = currDistance;
            }
            if (game.getGhostEdibleTime(ghost) > 0) {
                esiste_edible_ghost = true;
                if (distance_edible > currDistance) {
                    distance_edible = currDistance;
                    edible_ghost = ghost;
                }
            }
        }
        
        if (esiste_edible_ghost && game.wasGhostEaten(edible_ghost)){
            utility += game.getGhostCurrentEdibleScore();
        }
//        if (esiste_edible_ghost) {
//            utility -= distance_edible * 4;
//        }

        if (game.wasPacManEaten()) {
            utility -= 500;
        }

        if (game.wasPacManEaten() && game.getPacmanNumberOfLivesRemaining() == 0) {
            utility -= 15000;
        }

        return utility;
    }

    private static double computeUtility5(Game game) {
        double distance = Double.POSITIVE_INFINITY;
        double currDistance;
        GHOST nearestGhost = null;
        double utility = game.getScore();

//            for(GHOST ghost : GHOST.values()){      
//                currDistance = game.getDistance(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost), Constants.DM.EUCLID);
//                if(currDistance < distance){
//                    distance = currDistance;
//                    nearestGhost = ghost;
//                }
//            }
//                        
//            if(game.getGhostEdibleTime(nearestGhost)==0){
//                if (distance < 25)
//                    utility += distance*5;
//            }else{
//                utility -= distance*4;
//            }
        int index_power_pillola = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), Constants.DM.PATH);
        if (index_power_pillola > -1) {
            double dist_power_pillola = game.getDistance(game.getPacmanCurrentNodeIndex(), index_power_pillola, Constants.DM.PATH);
            utility -= dist_power_pillola * 5;
        }

        if (game.wasPacManEaten()) {
            utility -= 500;
        }

        if (game.wasPacManEaten() && game.getPacmanNumberOfLivesRemaining() == 0) {
            utility -= 15000;
        }

        return utility;
    }

    /*
     * Consider all possible moves by the ghosts
     */
    private double getMin2ScoreAlphaBeta(Game s, int depth, double alpha, double beta, long endTime) throws Exception {
        double res;
        double score;
        double distance = Double.POSITIVE_INFINITY;
        double currDistance;
        GHOST nearestGhost = null;
        if (func == 1) {
            res = computeUtility(s);
        } else if (func == 2) {
            res = computeUtility2(s);
        } else if (func == 3) {
            res = computeUtility3(s);
        } else {
            res = s.getScore();
        }

        if (depth == 0 || s.wasPacManEaten()) {
            return res;
        } else {
            //res = Double.POSITIVE_INFINITY;
            score = res;

            for (GHOST ghost : GHOST.values()) {
                currDistance = s.getDistance(s.getPacmanCurrentNodeIndex(), s.getGhostCurrentNodeIndex(ghost), Constants.DM.EUCLID);
                if (currDistance < distance) {
                    distance = currDistance;
                    nearestGhost = ghost;
                }
            }

            GHOST g = nearestGhost;
            MOVE[] array = s.getPossibleMoves(s.getGhostCurrentNodeIndex(g), s.getGhostLastMoveMade(g));
            List<MOVE> legalMoves = Arrays.asList(array);
            for (MOVE move : legalMoves) {
                if (System.currentTimeMillis() > endTime) {
                    return -1000;
                }
                EnumMap<GHOST, MOVE> map = new EnumMap<>(GHOST.class);
                map.put(g, move);
                Game next = s.copy();
                next.updateGhosts(map);
                next.updateGame();

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
