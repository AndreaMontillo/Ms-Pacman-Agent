/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.entries.pacman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 *
 * @author franc
 */
public class MyPacman_minmax extends Controller<Constants.MOVE> {

    private Constants.MOVE myMove = Constants.MOVE.NEUTRAL;
    private static final int MIN_DISTANCE = 30;	//if a ghost is this close, run away

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {

        //return getMove2(game, timeDue);
        double distance = Double.POSITIVE_INFINITY;
        double distance_edible = Double.POSITIVE_INFINITY;
        double currDistance;
        GHOST nearestGhost = null;
        int[] index_ghosts = new int[4];
        double[] dist = new double[4];
//        boolean flag = false;
        boolean esiste_edible_ghost = false;

        int k = 0;
        for (GHOST ghost : GHOST.values()) {
            currDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), Constants.DM.PATH);
            dist[k] = currDistance;
            index_ghosts[k++] = game.getGhostCurrentNodeIndex(ghost);

            if (currDistance < distance) {
                distance = currDistance;
                nearestGhost = ghost;
            }
            if (game.getGhostEdibleTime(ghost) >= 0) {
                esiste_edible_ghost =true;
                if (distance_edible > currDistance) {
                    distance_edible = currDistance;
                }
            }

        }
        Arrays.sort(dist);
        int index_power_pillola = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), Constants.DM.PATH);

        double dist_ghost = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), Constants.DM.PATH);

        double dist_power_pillola = -1;
        double dist_ghost_power_pill = -1;
        if (index_power_pillola > 0) {
            int index_ghostToPowerPill = game.getClosestNodeIndexFromNodeIndex(index_power_pillola, index_ghosts, Constants.DM.PATH);
            dist_power_pillola = game.getDistance(game.getPacmanCurrentNodeIndex(), index_power_pillola, Constants.DM.PATH);
            dist_ghost_power_pill = game.getDistance(index_ghostToPowerPill, index_power_pillola, Constants.DM.PATH);

            //REGOLA 0 5,4,6 (distanza tra pacman e fantasma più vicino alla power pill)
            if (dist_power_pillola <= 4 && dist_ghost >= 6 && dist_ghost_power_pill >= 8) {
                return game.getPacmanLastMoveMade().opposite(); //mangiati le pillole se non ci sono fantasmi vicini
            }
        }

        //Strategy 1: if any non-edible ghost is too close (less than MIN_DISTANCE), run away

        if (game.getGhostEdibleTime(nearestGhost) == 0 && game.getGhostLairTime(nearestGhost) == 0) {
            if (dist[0] < MIN_DISTANCE) {
                if (index_power_pillola > 0 && dist_power_pillola > 5 && dist_ghost_power_pill > dist_power_pillola && dist[1] < MIN_DISTANCE && dist[2] < MIN_DISTANCE) {
                    //return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), index_power_pillola, Constants.DM.PATH);
                    MiniMax minimax = new MiniMax(timeDue, 5); //vai verso power pill
                    try {
                        myMove = minimax.getAction(game);
                    } catch (Exception ex) {
                        Logger.getLogger(MyPacman_minmax.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return myMove;
                } else {
                    MiniMax minimax = new MiniMax(timeDue, 1);
                    try {
                        myMove = minimax.getAction(game);
                    } catch (Exception ex) {
                        Logger.getLogger(MyPacman_minmax.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return myMove;
                }

            }
        }

        //Strategy 2: find the nearest edible ghost and go after them 

        if (esiste_edible_ghost) //we found an edible ghost
        {
            //return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(minGhost), Constants.DM.PATH);
            MiniMax minimax = new MiniMax(timeDue, 3);
            try {
                myMove = minimax.getAction(game);
            } catch (Exception ex) {
                Logger.getLogger(MyPacman_minmax.class.getName()).log(Level.SEVERE, null, ex);
            }
            return myMove;
        }

//        boolean flag = true;
//        for (GHOST ghost: GHOST.values()){
//            if (game.getGhostLairTime(ghost) > 0) 
//                flag = false;
//            else{
//                flag = true;
//                break;
//            }
//        }
//        
//        if (!flag){
        //Strategy 3: go after the pills and power pills
        int[] pills = game.getPillIndices();
        int[] powerPills = game.getPowerPillIndices();

        ArrayList<Integer> targets = new ArrayList<Integer>();

        for (int i = 0; i < pills.length; i++) //check which pills are available			
        {
            if (game.isPillStillAvailable(i)) {
                targets.add(pills[i]);
            }
        }

        for (int i = 0; i < powerPills.length; i++) //check with power pills are available
        {
            if (game.isPowerPillStillAvailable(i)) {
                targets.add(powerPills[i]);
            }
        }

        int[] targetsArray = new int[targets.size()];		//convert from ArrayList to array

        for (int i = 0; i < targetsArray.length; i++) {
            targetsArray[i] = targets.get(i);
        }

        //return the next direction once the closest target has been identified
        int index = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), targetsArray, Constants.DM.PATH);

//            if (game.getPowerPillIndex(index) != -1){
//                MiniMax minimax  = new MiniMax(timeDue,1);
//                    try {
//                        myMove = minimax.getAction(game);
//                    } catch (Exception ex) {
//                        Logger.getLogger(MyPacman_minmax.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    return myMove;
//                }
        return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), index, Constants.DM.PATH);
//        }else{
//            MiniMax minimax  = new MiniMax(timeDue,1);
//                try {
//                    myMove = minimax.getAction(game);
//                } catch (Exception ex) {
//                    Logger.getLogger(MyPacman_minmax.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            return myMove;
//        }
    }

    public Constants.MOVE getMove2(Game game, long timeDue) {

        double distance = Double.POSITIVE_INFINITY;
        double distance_edible = Double.POSITIVE_INFINITY;
        double currDistance;
        GHOST nearestGhost = null;
        int[] index_ghosts = new int[4];
        boolean flag = false;
        boolean esiste_edible_ghost = false;

        int k = 0;
        for (GHOST ghost : GHOST.values()) {
            index_ghosts[k++] = game.getGhostCurrentNodeIndex(ghost);
            currDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), Constants.DM.EUCLID);
            if (currDistance < distance) {
                distance = currDistance;
                nearestGhost = ghost;
            }
            if (game.getGhostEdibleTime(ghost) > 0) {
                esiste_edible_ghost = true;
                if (distance_edible > currDistance) {
                    distance_edible = currDistance;
                }
            }
            if (game.getGhostLairTime(ghost) > 0) {
            } else {
                flag = true;
            }
        }

        int index_power_pillola = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), Constants.DM.EUCLID);
        double dist_ghost = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), Constants.DM.EUCLID);

        if (index_power_pillola > 0) {
            int index_ghostToPowerPill = game.getClosestNodeIndexFromNodeIndex(index_power_pillola, index_ghosts, Constants.DM.PATH);

            double dist_power_pillola = game.getDistance(game.getPacmanCurrentNodeIndex(), index_power_pillola, Constants.DM.EUCLID);
            double dist_ghost_power_pill = game.getDistance(game.getPacmanCurrentNodeIndex(), index_ghostToPowerPill, Constants.DM.EUCLID);

            //REGOLA 1
            if (dist_power_pillola <= 5 && dist_ghost >= 4 && dist_ghost_power_pill >= 6) {
                return game.getPacmanLastMoveMade().opposite(); //prima era neutral
            }

            //REGOLA 2 e 3
            boolean esiste_pillola = game.getNumberOfActivePills() > 0;
            if (esiste_pillola && dist_ghost <= 8 && dist_power_pillola <= dist_ghost_power_pill) {
                MiniMax minimax = new MiniMax(timeDue, 2);
                try {
                    //FARE UN MINIMAx AD HOC: verso power pill più vicina
                    myMove = minimax.getAction(game);
                } catch (Exception ex) {
                    Logger.getLogger(MyPacman_minmax.class.getName()).log(Level.SEVERE, null, ex);
                }
                return myMove;
            }
        }
        //REGOLA 4 e 6
        if (esiste_edible_ghost && distance_edible <= 8) {
            MiniMax minimax = new MiniMax(timeDue, 3);
            try {
                //FARE UN MINIMAx AD HOC: verso power pill più vicina
                myMove = minimax.getAction(game);
            } catch (Exception ex) {
                Logger.getLogger(MyPacman_minmax.class.getName()).log(Level.SEVERE, null, ex);
            }
            return myMove;
        }

//        //REGOLA 5
//        if (dist_ghost <= 8){
//            MiniMax minimax  = new MiniMax(timeDue,5);
//            try {
//                //FARE UN MINIMAx AD HOC: verso pillola più vicina
//                myMove = minimax.getAction(game);
//            } catch (Exception ex) {
//                Logger.getLogger(MyPacman_minmax.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            return myMove;
//        }
        //REGOLA 7
        if (dist_ghost >= 9) {
            MiniMax minimax = new MiniMax(timeDue, 5);
            try {
                //FARE UN MINIMAx AD HOC: verso pillola più vicina
                myMove = minimax.getAction(game);
            } catch (Exception ex) {
                Logger.getLogger(MyPacman_minmax.class.getName()).log(Level.SEVERE, null, ex);
            }
            return myMove;
        }

        if (!flag) {
            //Strategy 3: go after the pills and power pills
            int[] pills = game.getPillIndices();
            int[] powerPills = game.getPowerPillIndices();

            ArrayList<Integer> targets = new ArrayList<Integer>();

            for (int i = 0; i < pills.length; i++) //check which pills are available			
            {
                if (game.isPillStillAvailable(i)) {
                    targets.add(pills[i]);
                }
            }

            for (int i = 0; i < powerPills.length; i++) //check with power pills are available
            {
                if (game.isPowerPillStillAvailable(i)) {
                    targets.add(powerPills[i]);
                }
            }

            int[] targetsArray = new int[targets.size()];		//convert from ArrayList to array

            for (int i = 0; i < targetsArray.length; i++) {
                targetsArray[i] = targets.get(i);
            }

            //return the next direction once the closest target has been identified
            return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), targetsArray, Constants.DM.PATH), Constants.DM.PATH);
        } else {
            MiniMax minimax = new MiniMax(timeDue, 1);
            try {
                myMove = minimax.getAction(game);
            } catch (Exception ex) {
                Logger.getLogger(MyPacman_minmax.class.getName()).log(Level.SEVERE, null, ex);
            }
            return myMove;
        }

    }

}
