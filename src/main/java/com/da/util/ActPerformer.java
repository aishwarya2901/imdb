package com.da.util;


import java.util.ArrayList;
import java.util.List;

public class ActPerformer {
    private final String name;
    private List<Movie> actedIn;

    public ActPerformer(String name) {
        this.name = name;
        this.actedIn = new ArrayList<Movie>();
    }

    public void addActedIn(Movie movie){
        this.actedIn.add(movie);
    }

    public void addActedIn(List<Movie> movies){
        this.actedIn.addAll(movies);
    }

    public String getName() {
        return name;
    }

    public List<Movie> getActedIn() {
        return actedIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActPerformer)) return false;

        ActPerformer that = (ActPerformer) o;

        if (!getName().equals(that.getName())) return false;
        return getActedIn().equals(that.getActedIn());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getActedIn().hashCode();
        return result;
    }
}
