package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.List;

public class SyncedToDoCRUDOperations implements ToDoCRUDOperations {

    private ToDoCRUDOperations localOperations;
    private ToDoCRUDOperations remoteOperations;

    public SyncedToDoCRUDOperations(ToDoCRUDOperations localOperations, ToDoCRUDOperations remoteOperations) {
        this.localOperations = localOperations;
        this.remoteOperations = remoteOperations;
    }

    @Override
    public ToDo createToDo(ToDo item) {
        ToDo created = localOperations.createToDo(item);
        remoteOperations.createToDo(created);
        return created;
    }

    @Override
    public List<ToDo> readAllToDos() {
        return localOperations.readAllToDos();
    }

    @Override
    public ToDo readToDo(long id) {
        return localOperations.readToDo(id);
    }

    @Override
    public ToDo updateToDo(ToDo todoToUpdate) {
        ToDo updated = localOperations.updateToDo(todoToUpdate);
        remoteOperations.updateToDo(updated);
        return updated;
    }

    @Override
    public boolean deleteToDo(long id) {
        if (localOperations.deleteToDo(id)) {
            return remoteOperations.deleteToDo(id);
        } else {
            return false;
        }
    }
}
