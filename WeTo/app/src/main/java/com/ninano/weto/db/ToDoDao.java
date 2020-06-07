package com.ninano.weto.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

//Dao = Data Access Object  = db접근 객체
@Dao
public abstract class ToDoDao {
    @Query("SELECT * FROM ToDo")
    abstract LiveData<List<ToDo>> getAll();

    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo ORDER BY Todo.ordered")
    public abstract LiveData<List<ToDoWithData>> getTodoList();

    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo ORDER BY Todo.ordered")
    public abstract List<ToDoWithData> getTodoListNoLive();

    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo and Todo.todoNo = :todoNo")
    public abstract List<ToDoWithData> getTodoWithTodoNo(int todoNo);

    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo and ToDoData.isWiFi = :isWifi and ToDoData.locationMode = :locationMode")
    public abstract List<ToDoWithData> getTodoWithWifi(char isWifi, int locationMode);

    @Query("SELECT count(*) FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo and ToDoData.isWiFi = :isWifi and ToDoData.locationMode = :locationMode")
    public abstract int getTodoWithWifiCount(char isWifi, int locationMode);

    //활성중인 일정조회
    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.status = 'ACTIVATE' ORDER BY Todo.ordered")
    public abstract LiveData<List<ToDoWithData>> getActivatedTodoList();

    //종료된 일정조회
    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.status = 'DONE'")
    public abstract LiveData<List<ToDoWithData>> getDoneTodoList();


    //활성중인 일정조회 (livedata아닌버전)
    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.status = 'ACTIVATE' ORDER BY Todo.ordered")
    public abstract List<ToDoWithData> getActivatedTodoListNoLive();

    // 일정 완료
    @Query("UPDATE ToDo SET status = 'DONE' WHERE todoNo = :todoNo")
    public abstract void updateStatusDone(int todoNo);

    // 일정 완료x
    @Query("UPDATE ToDo SET status = 'ACTIVATE' WHERE todoNo = :todoNo")
    public abstract void updateStatusActivate(int todoNo);

    //순서 변경
    @Query("UPDATE ToDo SET ordered = :order WHERE todoNo = :todoNo AND status = 'ACTIVATE'")
    public abstract void updateOrder(int order, int todoNo);


    //종료된 일정조회
//    @Query("SELECT todoNo, latitude, longitude, locationMode, radius FROM  ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.status = 'DONE' ORDER BY Todo.ordered")
//    public abstract LiveData<List<ToDoData>> getGpsTodo();


    @Transaction
    public int insertTodo(ToDo todo, ToDoData toDoData) {
        int todoNo = (int) insert(todo);
        toDoData.setTodoNo(todoNo);
        insertLocation(toDoData);
        return todoNo;
    }

    @Transaction
    public void updateTodo(ToDo todo, ToDoData toDoData){
        update(todo);
        updateTodoData(toDoData);
    }

    @Transaction
    public void deleteTodo(ToDo todo, ToDoData toDoData){
        delete(todo);
        deleteTodoData(toDoData);
    }



    @Insert
    abstract long insert(ToDo todo);

    @Insert
    abstract void insertLocation(ToDoData toDoData);

    @Update
    abstract void update(ToDo todo);

    @Update
    abstract void updateTodoData(ToDoData toDoData);

    @Delete
    abstract void delete(ToDo todo);

    @Delete
    abstract void deleteTodoData(ToDoData toDoData);

}
