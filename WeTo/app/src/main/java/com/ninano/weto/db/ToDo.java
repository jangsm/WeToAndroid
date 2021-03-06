package com.ninano.weto.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Todo")
public class ToDo {
    @PrimaryKey(autoGenerate = true)
    private int todoNo;
    private String title;
    private String content;
    private int icon;
    private int type;
    private String status;
    private int ordered;
    private char isImportant;

    //그룹 일정용
    private char isGroup; // 그룹아니면 N
    private String groupTodoCreator;
    private int groupNo;

    //그룹용 생성자
    public ToDo(String title, String content, int icon, int type, char isImportant, char isGroup) {
        this.title = title;
        this.content = content;
        this.icon = icon;
        this.type = type;
        this.status = "ACTIVATE";
        this.ordered = 0;
        this.isImportant = isImportant;
        this.isGroup = isGroup;
    }

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    //로컬용 생성자
    @Ignore
    public ToDo(String title, String content, int icon, int type, char isImportant) {
        this.title = title;
        this.content = content;
        this.icon = icon;
        this.type = type;
        this.status = "ACTIVATE";
        this.ordered = 0;
        this.isImportant = isImportant;
        this.isGroup = 'N';
    }

    public String getGroupTodoCreator() {
        return groupTodoCreator;
    }

    public void setGroupTodoCreator(String groupTodoCreator) {
        this.groupTodoCreator = groupTodoCreator;
    }

    public void setTodoNo(int todoNo) {
        this.todoNo = todoNo;
    }


    public int getTodoNo() {
        return todoNo;
    }

    public void setNo(int no) {
        this.todoNo = no;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrder() {
        return ordered;
    }

    public void setOrder(int order) {
        this.ordered = order;
    }

    public int getNo() {
        return todoNo;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getIcon() {
        return icon;
    }

    public int getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }


    public int getOrdered() {
        return ordered;
    }

    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }


    public char getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(char isImportant) {
        this.isImportant = isImportant;
    }

    public char getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(char isGroup) {
        this.isGroup = isGroup;
    }


    @NonNull
    @Override
    public String toString() {
        return this.title + " " + content + " " + icon + " " + type + " " + status + " " + "\n";
    }
}
