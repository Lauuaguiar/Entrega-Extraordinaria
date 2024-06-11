package org.example.control;


public class Main {

    public static void main(String[] args) throws MyEventException {
        DatalakeControl datalakeControl = new DatalakeControl(args[0]);
        datalakeControl.createDataLake();
    }
}
