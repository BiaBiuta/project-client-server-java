package org.example;


import java.util.Objects;

public class Entity<ID>  {
    protected ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
    @Override
    public int hashCode(){
        return Objects.hash(getId());
    }
    @Override
    public boolean equals(Object obj){
        if(this==obj){
            return true;
        }
        if(!(obj instanceof Entity)) return false;
        Entity<?> entity=(Entity<?>)obj;
        return getId().equals(entity.getId());
    }
}
