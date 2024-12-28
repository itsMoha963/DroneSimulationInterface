package src.main.utils;

import src.main.core.DroneBase;

import java.util.ArrayList;

public class BaseDroneFilter<T extends DroneBase> {
    private ArrayList<DroneFilterCondition<T>> conditions = new ArrayList<>();

    public void addCondition(DroneFilterCondition<T> condition) {
        conditions.add(condition);
    }

    public ArrayList<DroneFilterCondition<T>> getConditions() {
        return conditions;
    }
}
