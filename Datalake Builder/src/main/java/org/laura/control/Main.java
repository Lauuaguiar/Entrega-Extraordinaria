package org.laura.control;


public class Main {

    public static void main(String[] args){
        DatalakeControl datalakeControl = new DatalakeControl(args[0]);
        datalakeControl.createDataLake();
    }
}
