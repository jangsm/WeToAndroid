package com.ninano.weto.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.ninano.weto.src.common.models.ServerTodoWithTodoNo;

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

    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo and ToDoData.isWiFi = :isWifi and ToDoData.locationMode = :locationMode AND Todo.status = 'ACTIVATE'")
    public abstract List<ToDoWithData> getTodoWithWifi(char isWifi, int locationMode);

    @Query("SELECT count(*) FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo and ToDoData.isWiFi = :isWifi and ToDoData.locationMode = :locationMode AND Todo.status = 'ACTIVATE'")
    public abstract int getTodoWithWifiCount(char isWifi, int locationMode);

    //존재하는 알람인지 확인
    @Query("SELECT count(*) FROM ToDo WHERE todoNo = :todoNo AND status = 'ACTIVATE'")
    public abstract int getTodoWithAlarmCount(int todoNo);

    //활성중인 개인일정조회
    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.status = 'ACTIVATE' AND Todo.isGroup = 78 ORDER BY Todo.ordered")
    public abstract LiveData<List<ToDoWithData>> getActivatedTodoList();

    //종료된 개인일정조회
    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.status = 'DONE' AND Todo.isGroup = 78")
    public abstract LiveData<List<ToDoWithData>> getDoneTodoList();

    //활성중인 그룹일정조회
    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.status = 'ACTIVATE' AND Todo.isGroup = 89 ORDER BY Todo.ordered")
    public abstract LiveData<List<ToDoWithData>> getActivatedGroupTodoList();

    //종료된 그룹일정조회
    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.status = 'DONE' AND Todo.isGroup = 89")
    public abstract LiveData<List<ToDoWithData>> getDoneGroupTodoList();

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

    //자주가는 장소 조회
    @Query("SELECT * FROM FavoriteLocation")
    public abstract LiveData<List<FavoriteLocation>> getFavoriteLocation();

    //활성중인 일정조회
    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.status = 'ACTIVATE' AND (Todo.type = 77 OR Todo.type = 88) ORDER BY Todo.ordered")
    public abstract LiveData<List<ToDoWithData>> getActivatedLocationTodoList();

    // Todo 삭제
    @Query("DELETE FROM ToDo WHERE todoNo = :todoNo")
    public abstract void deleteToDo(int todoNo);

    //ToDoData 삭제
    @Query("DELETE FROM ToDoData WHERE todoDataNo = :toDoDataNo")
    public abstract void deleteToDoData(int toDoDataNo);

    //활성중인 특정 그룹일정조회
    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.status = 'ACTIVATE' AND Todo.isGroup = 89 AND Todo.groupNo = :groupNo ORDER BY Todo.ordered")
    public abstract LiveData<List<ToDoWithData>> getActivatedGroupTodoList(int groupNo);

    //종료된 특정 그룹일정조회
    @Query("SELECT * FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.status = 'DONE' AND Todo.isGroup = 89 AND Todo.groupNo = :groupNo")
    public abstract LiveData<List<ToDoWithData>> getDoneGroupTodoList(int groupNo);

    //모든 그룹일정 조회(livedata아닌버전)
    @Query("SELECT ToDoData.severTodoNo FROM ToDo INNER JOIN ToDoData WHERE Todo.todoNo =  ToDoData.todoNo AND Todo.isGroup = 89 ORDER BY ToDoData.severTodoNo")
    public abstract List<Integer> getAllGroupTodo();

    //serverTodoNo 중복 검사
    @Query("SELECT ToDoData.severTodoNo, TodoData.todoNo FROM ToDoData WHERE ToDoData.severTodoNo = :serverTodoNo")
    public abstract List<ServerTodoWithTodoNo> checkServerTodoNo(int serverTodoNo);

    //일정 업데이트(그룹 Todo 용)
    @Query("UPDATE ToDo SET title = :title, content = :content, type = :type, isImportant = :isImportant" +
            " WHERE todoNo = :todoNo AND status = 'ACTIVATE'")
    public abstract void updateTodoByServer(int todoNo, String title, String content, int type, int isImportant);

    //일정 업데이트(그룹 TodoData 용)
    @Query("UPDATE ToDoData SET locationName = :locationName, latitude = :latitude, longitude = :longitude, locationMode = :locationMode," +
            " radius = :radius, ssid = :ssid, isWiFi = :isWiFi, timeSlot = :timeSlot, repeatType = :repeatType, repeatDayOfWeek = :repeatDayOfWeek, repeatDay = :repeatDay, " +
            " date = :date,  time = :time,  year = :year,  month = :month, day = :day, hour = :hour, minute = :minute, minute = :minute, meetRemindTime = :meetRemindTime WHERE severTodoNo = :severTodoNo")
    public abstract void updateTodoDataByServer(String locationName, double latitude, double longitude, int locationMode, int radius, String ssid, char isWiFi, int timeSlot, int repeatType, String repeatDayOfWeek, int repeatDay, String date, String time
            , int year, int month, int day, int hour, int minute, int meetRemindTime, int severTodoNo);

    //일정 정보 업데이트 (덮어쓰기)
    @Transaction
    public void updateGroupTodo(ToDo todo, ToDoData toDoData, int todNo, int serverTodoNo) {
        updateTodoByServer(todNo, todo.getTitle(), todo.getContent(), todo.getType(), todo.getIsImportant());
        updateTodoDataByServer(toDoData.getLocationName(), toDoData.getLatitude(), toDoData.getLongitude(), toDoData.getLocationMode(), toDoData.getRadius(), toDoData.getSsid(), toDoData.getIsWiFi(), toDoData.getTimeSlot(), toDoData.getRepeatType(), toDoData.getRepeatDayOfWeek(), toDoData.getRepeatDay(), toDoData.getDate(), toDoData.getTime()
                , toDoData.getYear(), toDoData.getMonth(), toDoData.getDay(), toDoData.getHour(), toDoData.getMinute(), toDoData.getMeetRemindTime(), serverTodoNo);
    }
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
    public void updateTodo(ToDo todo, ToDoData toDoData) {
        update(todo);
        updateTodoData(toDoData);
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

    @Insert
    public abstract long insert(FavoriteLocation todo);

    @Update
    public abstract void update(FavoriteLocation todo);

    @Delete
    abstract void deleteTodoData(ToDoData toDoData);

}
