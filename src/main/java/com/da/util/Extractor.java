package com.da.util;

import org.javatuples.Pair;

import java.util.List;

import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public enum Extractor {
    Simple(new SimpleExtractor());

    private final ExtractContent body;
    Extractor(ExtractContent body){
        this.body = body;
    }

    public Pair<String, List<Movie>> extract(String line){
        return body.extract(line);
    }

    public static Pair<String, List<Movie>> extractFromAll(String line){
        for (Extractor extractor : values()) {
            Pair<String, List<Movie>> extract = extractor.extract(line);
            if(extract != null){
                return extract;
            }
        }
        return null;
    }
}

interface ExtractContent{
    public Pair<String, List<Movie>> extract(String line);
}

class SimpleExtractor implements ExtractContent{

    public Pair<String, List<Movie>> extract(String line) {
        String[] arr = line.split("\\t");
        String[] strings = stream(arr).filter(x -> !x.trim().isEmpty())
                .toArray(String[]::new);
        if(strings.length==0){
            return null;
        }else if(strings.length == 1){
            return Pair.with(strings[0],null);
        }
        else {
            String[] movies =  copyOfRange(strings, 1, strings.length);
            List<Movie> collect = stream(movies).map(MovieUtils::getMovie).collect(toList());
            return Pair.with(strings[0], collect);
        }
    }
}
