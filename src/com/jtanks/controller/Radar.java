package com.jtanks.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.jtanks.model.BasicEntity;
import com.jtanks.model.Mine;
import com.jtanks.model.Missile;
import com.jtanks.model.MovingEntity;
import com.jtanks.model.Quartermaster;
import com.jtanks.model.Tank;
import com.jtanks.util.Euclid;
import com.jtanks.util.Point;

public class Radar {
    private final Point virtualLocation;
    private final double radarRadius;
    private final Quartermaster quartermaster;
    
    private List<EntityVector> enemyTankVectors;
    private List<EntityVector> missileVectors;
    private Collection<Mine> visibleMines;

    public Radar(Tank tank, Point virtualLocation, double radarRadius, Quartermaster quartermaster) {
        this.virtualLocation = virtualLocation;
        this.radarRadius = radarRadius;
        this.quartermaster = quartermaster;
        createEnemyTankVectors(tank);
        createMissileVectors();
        findVisibleMines();
    }

    public List<EntityVector> getEnemyTankVectors() {
        return enemyTankVectors;
    }


    public Collection<Mine> getMines() {
        return visibleMines;
    }
    
    public List<EntityVector> getMissileVectors() {
        return missileVectors;
    }
    
    private void findVisibleMines() {
        visibleMines = getMinesInRange(virtualLocation, radarRadius);
    }

    private void createMissileVectors() {
        Collection<Missile> allMissilesInRange = getMissilesInRange(virtualLocation, radarRadius);
        missileVectors = createEntityVectors(allMissilesInRange);
    }
    
    private void createEnemyTankVectors(Tank tank) {
        Collection<Tank> allTanksInRange = getLiveTanksInRange(virtualLocation, radarRadius);
        allTanksInRange.remove(tank);
        enemyTankVectors = createEntityVectors(allTanksInRange);
    }

    private List<EntityVector> createEntityVectors(Collection<? extends MovingEntity> entities) {
        List<EntityVector> entityVectors = new ArrayList<EntityVector>();
        
        for (MovingEntity entity : entities) {
            entityVectors.add(new EntityVector(entity));
        }
        
        return entityVectors;
    }

    private Collection<Tank> getLiveTanksInRange(Point center, double radius) {
        return getEntitiesInRange(getLiveTanks(), center, radius);
    }
    private Set<Tank> getLiveTanks() {
        Set<Tank> liveTanks = new HashSet<Tank>();
        for (Tank tank : quartermaster.getTanks()) {
            if (!tank.isDestroyed()) {
                liveTanks.add(tank);
            }
        }
        return liveTanks;
    }

    private Collection<Missile> getMissilesInRange(Point center, double radius) {
        return getEntitiesInRange(quartermaster.getMissiles(), center, radius);
    }
    
    private Collection<Mine> getMinesInRange(Point center, double radius) {
        return getEntitiesInRange(quartermaster.getMines(), center, radius); 
    }

    private <T extends BasicEntity> Collection<T> getEntitiesInRange(Collection<T> entities, Point center, double radius) {
        Set<T> entitiesInRange = new HashSet<T>();
        Iterator<T> iterator = entities.iterator();
        while (iterator.hasNext()) {
            T entity = (T) iterator.next();
            if (Euclid.circleContainsPoint(center, radius, entity.getPosition())) {
                entitiesInRange.add(entity);
            }
        }
        
        return entitiesInRange;
    }
    
}
