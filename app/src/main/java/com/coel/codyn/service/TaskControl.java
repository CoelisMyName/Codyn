package com.coel.codyn.service;

public interface TaskControl extends Runnable{

    boolean taskPause();

    boolean taskCancel();

    boolean taskResume();

}
