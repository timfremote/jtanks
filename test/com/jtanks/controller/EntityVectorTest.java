package com.jtanks.controller;

import com.jtanks.util.Point;

import org.junit.Test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;


public class EntityVectorTest {
    @Test public void
     constructorInitialisesFields() {
         Point point = new Point(1.0, 2.0);
         double speed = 3.0;
         double heading = 4.0;

         EntityVector entityVector = new EntityVector(point, speed, heading);

         assertThat(entityVector, allOf(hasProperty("position", sameInstance(point)),
                                        hasProperty("speed", equalTo(speed)),
                                        hasProperty("heading", equalTo(heading))));
     }

}
