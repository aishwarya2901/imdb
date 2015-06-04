package com.da;

import com.da.util.*;
import org.javatuples.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;


public class Main {
    public static void main(String[] args) throws Exception {

        Main m = new Main();
        List<ActPerformer> all = new ArrayList<>(20000);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("merged.txt")))) {
            final AtomicLong id = new AtomicLong(0);
            List<ActPerformer> actors = m.process(getFile("actor_movies.txt"), false, id);
            System.out.println("Total number of actors :" + actors.size());

            List<ActPerformer> actresses = m.process(getFile("actress_movies.txt"), true, id);
            System.out.println("Total number of actresses :" + actresses.size());

            all.addAll(actors);
            actresses.addAll(actresses);
        }

        for(ActPerformer act : all){
            
        }
    }

    private static int write(BufferedWriter writer, List<ActPerformer> actors) throws IOException {
        int count = 0;
        for (ActPerformer a : actors) {
            if (a.getActedIn().size() >= 5) {
                writer.write(a.getName());
                writer.newLine();
                count++;
            }
        }
        return count;
    }

    private static File getFile(String file) {
        return new File(Thread.currentThread().getContextClassLoader().getResource(file).getFile());
    }


    private List<ActPerformer> process(File file, boolean isMale, AtomicLong id) throws Exception {
        final HashMap<String, ActPerformer> actors = new HashMap<>();
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = r.readLine()) != null) {
                Pair<String, List<Movie>> ans = Extractor.extractFromAll(line);
                String name = ans.getValue0();
                actors.putIfAbsent(name, isMale
                        ? new Actor(name, id.incrementAndGet())
                        : new Actress(name, id. incrementAndGet()));
                ActPerformer actPerformer = actors.get(name);
                if (ans.getValue1() != null)
                    actPerformer.addActedIn(ans.getValue1());
            }
        }
        return actors.values().stream().collect(toList());
    }

}
