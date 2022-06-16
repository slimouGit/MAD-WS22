package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToDoCRUDOperationsImpl implements ToDoCRUDOperations {

    private static ToDoCRUDOperationsImpl instance;

    private Map<Long, ToDo> todoMap = new HashMap<>();
    private long idCount = 0;

    public static ToDoCRUDOperationsImpl getInstance(){
        if(null == instance){
            instance = new ToDoCRUDOperationsImpl();
        }
        return instance;
    }

    private ToDoCRUDOperationsImpl() {
        Arrays.asList("Lorem", "Ipsum", "Olar", "Pipsum", "Enfis", "Lorem").
                forEach(name -> this.createToDo(new ToDo(name)));
    }

    @Override
    public ToDo createToDo(ToDo item) {
        item.setId(++idCount);
        todoMap.put(item.getId(), item);
        return item;
    }

    @Override
    public List<ToDo> readAllToDos() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(todoMap.values());
    }

    @Override
    public ToDo readToDo(long id) {

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return todoMap.get(id);
    }

    @Override
    public ToDo updateToDo(ToDo todoToUpdate) {
        return todoMap.put(todoToUpdate.getId(),todoToUpdate);
    }

    @Override
    public boolean deleteToDo(long id) {
        todoMap.remove(id);
        return true;
    }
}
