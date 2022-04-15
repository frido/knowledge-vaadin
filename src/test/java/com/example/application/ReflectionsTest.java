package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import com.example.application.reflection.MyInvocationHandler;
import com.example.application.reflection.MyReflectionInterface;
import com.example.application.reflection.MyReflectionObject;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

/**
 * Try some reflexion features
 * 
 */
public class ReflectionsTest {

    private static final int LOOP_SIZE = 10_000_000;

    /**
     * Try performance of reflection
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void simplePerformance() throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        MyReflectionObject object = new MyReflectionObject();
        String value = null;

        StopWatch watchDirect = new StopWatch();
        watchDirect.start();
        for (int i = 0; i < LOOP_SIZE; i++) {
            value = object.getField2();
        }
        watchDirect.stop();

        StopWatch watchReflexion = new StopWatch();
        Method method = MyReflectionObject.class.getDeclaredMethod("getField2", new Class[] {});
        watchReflexion.start();
        for (int i = 0; i < LOOP_SIZE; i++) {
            value = (String) method.invoke(object);
        }
        watchReflexion.stop();

        StopWatch watchReflexionFull = new StopWatch();
        watchReflexionFull.start();
        for (int i = 0; i < LOOP_SIZE; i++) {
            Method method2 =
                    MyReflectionObject.class.getDeclaredMethod("getField2", new Class[] {});
            value = (String) method2.invoke(object);
        }
        watchReflexionFull.stop();

        StopWatch watchProxy = new StopWatch();
        InvocationHandler handler = new MyInvocationHandler(object);
        MyReflectionInterface proxy = (MyReflectionInterface) Proxy.newProxyInstance(
                MyReflectionInterface.class.getClassLoader(),
                new Class[] {MyReflectionInterface.class}, handler);
        watchProxy.start();
        for (int i = 0; i < LOOP_SIZE; i++) {
            value = proxy.getField2();
        }
        watchProxy.stop();

        System.out.println(value);
        System.out.println(watchDirect.getTime());
        System.out.println(watchReflexion.getTime());
        System.out.println(watchReflexionFull.getTime());
        System.out.println(watchProxy.getTime());

        assertTrue(watchDirect.getTime() < watchProxy.getTime());
    }

    /**
     * Load class and instanciate it.
     * 
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public void classLoaders() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        var className = "com.example.application.reflection.MyReflectionObject";
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        Constructor<?> constructor = clazz.getDeclaredConstructor( new Class[] {});
        MyReflectionInterface instance = (MyReflectionInterface) constructor.newInstance();
        assertEquals(MyReflectionObject.FIELD_2, instance.getField2());
    }
}
