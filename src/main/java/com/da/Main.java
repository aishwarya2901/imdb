package com.da;

import com.da.util.*;
import org.javatuples.Pair;

import java.io.*;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;


public class Main {
    public static void main(String[] args) throws Exception {

        Main m = new Main();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("merged.txt")))) {
            List<ActPerformer> actresses = m.process(getFile("actor_movies.txt"), false);
            System.out.println("Total number of actors :" + actresses.size());
            int count = write(writer, actresses);
            System.out.println("Total number of actors (>=5) :" + count);
            System.out.println("Total number of actress " + actresses.size());
            List<ActPerformer> actors = m.process(getFile("actress_movies.txt"), true);
            int count2 = write(writer, actors);
            System.out.println("Total number of actors (>=5) :" + count2);
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


    private List<ActPerformer> process(File file, boolean isMale) throws Exception {
        final HashMap<String, ActPerformer> actors = new HashMap<>();
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = r.readLine()) != null) {
                Pair<String, List<Movie>> ans = Extractor.extractFromAll(line);
                String name = ans.getValue0();
                actors.putIfAbsent(name, isMale
                        ? new Actor(name)
                        : new Actress(name));
                ActPerformer actPerformer = actors.get(name);
                if (ans.getValue1() != null)
                    actPerformer.addActedIn(ans.getValue1());
            }
        }
        return actors.values().stream().collect(toList());
    }

}
