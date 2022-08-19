package com.example.final1;

public class Data {
    String name,speed,room;
    public Data(){}
    public Data(String name, String speed,String room){
        this.name=name;
        this.speed=speed;
        this.room=room;
    }

    public String getName() {
        return name;
    }

    public String getRoom() {
        return room;
    }

    public String getSpeed() {
        return speed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
