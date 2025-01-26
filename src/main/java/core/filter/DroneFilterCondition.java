package core.filter;

import core.drone.DroneBase;

public interface DroneFilterCondition<T extends DroneBase> {
    boolean evaluate(T drone);
}
