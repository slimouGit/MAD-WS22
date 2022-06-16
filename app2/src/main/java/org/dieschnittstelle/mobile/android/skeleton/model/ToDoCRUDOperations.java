package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.List;

public interface ToDoCRUDOperations {

    public ToDo createToDo(ToDo item);

    public List<ToDo> readAllToDos();

    public ToDo readToDo(long id);

    public ToDo updateToDo(ToDo todoToUpdate);

    public boolean deleteToDo(long id);
}
