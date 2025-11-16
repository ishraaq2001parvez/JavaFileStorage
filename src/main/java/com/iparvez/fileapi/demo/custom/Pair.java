package com.iparvez.fileapi.demo.custom;

public class Pair<T,V>{
    /*
     * first and second items
     */
    private T first;
    private V second; 

    /*
     * generic constructors, setter and getter
     */
    public Pair(T first, V second) {
        this.first = first;
        this.second = second;
    }
    public static <T,V> Pair<T,V> of(T first, V second){
        return new Pair<T,V>(first, second); 
    }
    public void setFirst(T first){
        this.first = first; 
    }

    public void setSecond(V second){
        this.second = second; 
    }

    public T getFirst(){
        return this.first; 
    }
    public V getSecond(){
        return this.second ;
    }
    
    

}
