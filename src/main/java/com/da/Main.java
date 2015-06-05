package com.da;

import com.da.util.*;
import org.javatuples.Pair;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.mapping;
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

            ArrayList<ActPerformer> temp = new ArrayList<>();
            temp.addAll(actors);
            temp.addAll(actresses);

            temp.stream().filter(x -> x.getActedIn().size()>=5).forEach(x -> all.add(x));
        }
        printIds(all);
        System.out.println("Printed all ids");
        Map<Movie, List<ActPerformer>> map = new HashMap<>();

        for(ActPerformer act : all){
            act.getActedIn().stream().forEach(x -> {
                map.putIfAbsent(x, new ArrayList<>());
                List<ActPerformer> ls = map.get(x);
                ls.add(act);
            });
        }
        System.out.println("Printed all movies");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("final.txt")))) {
            for (ActPerformer a : all) {
                final Set<Pair<ActPerformer, ActPerformer>> over = new HashSet<>();

                for (Movie movie : a.getActedIn()) {
                    List<ActPerformer> ls = map.get(movie);
                    for (ActPerformer partner : ls) {
                        boolean recent = over.add(Pair.with(a, partner));
                        if(!a.equals(partner) && recent) {
                            final double common = common(a, partner) * 1.0;
                            writer.write(a.getId() + ",    " + partner.getId() + ",    " + common / a.getActedIn().size());
                            writer.newLine();
                        }
                    }
                }
            }
        }
    }

    private static int common(ActPerformer a1, ActPerformer a2){
        int ans = 0;
        for (Movie movie : a1.getActedIn()) {
            for (Movie movie1 : a2.getActedIn()) {
                if(movie.equals(movie1)){
                    ans++;
                }
            }
        }
        return ans;
    }



    private void printAllMovies(List<Movie> movies) throws IOException {
        Stream<Movie> sorted = movies.stream().sorted();
        try(BufferedWriter w = new BufferedWriter(new FileWriter(new File("movies.txt")))){
            sorted.forEach(x -> {
                try {
                    w.write(x.getName());
                    w.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static void printIds(List<ActPerformer> all) throws IOException {
        Stream<ActPerformer> sorted = all.stream().sorted((o1, o2) -> o1.getName().compareTo(o2.getName()));
        try(BufferedWriter w = new BufferedWriter(new FileWriter(new File("ids.txt")))){
            sorted.forEach(x -> {
                try {
                    w.write(x.getId() + "         -> " + x.getName());
                    w.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
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
