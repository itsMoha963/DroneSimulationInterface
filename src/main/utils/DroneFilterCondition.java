package src.main.utils;

import src.main.core.DroneBase;

public interface DroneFilterCondition<T extends DroneBase> {
    boolean evaluate(T drone);
}
