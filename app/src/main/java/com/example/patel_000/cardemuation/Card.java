package com.example.patel_000.cardemuation;

class Card {
    String name;
    String number;
    String password;
    String  cardID;
    public Card(){

    }
     public  Card(String cardID,String name,String number,String password){
         this.cardID=cardID;
         this.name=name;
         this.number=number;
         this.password=password;
     }

    public String getcardID() {
        return cardID;
    }
    public String toString() {
        String str = "{ \"name\":\"" + name + "\", \"cardnumber\":" + number + ", \"password\":" + password + ", \"idcardID\":" + cardID + "  }";
        return str;
    }
}
