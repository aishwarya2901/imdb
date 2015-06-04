package com.da.util;

import org.junit.Assert;
import org.junit.Test;

import static com.da.util.MovieUtils.getMovie;

public class MovieUtilsTest {

    @Test
    public void testGetMovie() throws Exception {
        Movie movie = getMovie("Little Angel (Angelita) (2014)");
        Assert.assertEquals(movie.getReleaseYear(),2014);
        Movie movie1 = getMovie("Limo Driver (????)  ");
        Assert.assertEquals(movie1.getReleaseYear(),-1);

        Movie movie2 = getMovie("Branded (????/I)");
        Assert.assertEquals(movie1.getReleaseYear(),-1);

    }
}