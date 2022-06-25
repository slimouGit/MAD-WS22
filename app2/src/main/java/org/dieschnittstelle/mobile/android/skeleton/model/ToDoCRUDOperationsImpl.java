package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToDoCRUDOperationsImpl implements ToDoCRUDOperations {

//    private static ToDoCRUDOperationsImpl instance;

    private ToDoCRUDOperations realCrudOperations;

    private Map<Long, ToDo> todoMap = new HashMap<>();


    public ToDoCRUDOperationsImpl(ToDoCRUDOperations realCrudOperations) {
        this.realCrudOperations = realCrudOperations;
    }

    @Override
    public ToDo createToDo(ToDo item) {
//        item.setId(++idCount);
        ToDo created = realCrudOperations.createToDo(item);
        todoMap.put(created.getId(), created);
        return item;
    }

    @Override
    public List<ToDo> readAllToDos() {
        if(todoMap.size() == 0){
            realCrudOperations.readAllToDos().forEach(
                    item -> {
                        todoMap.put(item.getId(), item);
                    }
            );
        }
        return new ArrayList<>(todoMap.values());
    }

    @Override
    public ToDo readToDo(long id) {
        if(!todoMap.containsKey(id)){
            ToDo item = realCrudOperations.readToDo(id);
            if(item != null){
                todoMap.put(item.getId(), item);
            }
            return item;
        }
        return todoMap.get(id);
    }

    @Override
    public ToDo updateToDo(ToDo todoToUpdate) {
        ToDo updated = this.realCrudOperations.updateToDo(todoToUpdate);
        todoMap.put(todoToUpdate.getId(), updated);
        return updated;
    }

    @Override
    public boolean deleteToDo(long id) {
        if(this.realCrudOperations.deleteToDo(id)){
            todoMap.remove(id);
            return true;
        }
        else{
            return false;
        }
    }
}
