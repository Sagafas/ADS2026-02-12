package by.it.group510901.petsevich.lesson13;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GraphA {
    public static void main(String[] args) throws IOException {
        Set<String> vertices = new TreeSet<>();
        Map<String, Set<String>> graph = new HashMap<>();

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        if(!input.isEmpty()) {
            String[] edges = input.split(",");
            for (String edge : edges) {
                String[] parts = edge.split("->");
                if (parts.length != 2) continue;
                String from = parts[0].trim();
                String to = parts[1].trim();

                vertices.add(from);
                vertices.add(to);

                if (!graph.containsKey(from)) {
                    HashSet<String> neighbors = new HashSet<>();
                    neighbors.add(to);
                    graph.put(from, neighbors);
                } else graph.get(from).add(to);
            }
        }

        List<String> result = topologicalSort(graph, vertices);

        for(String v : result)
            System.out.print(v + " ");
    }

    private static List<String> topologicalSort(Map<String, Set<String>> graph, Set<String> vertices) {
        Map<String, Integer> inDegree = new HashMap<>();
        for(String v : vertices)
            inDegree.put(v, 0);

        for(var neighbors : graph.values())
            for(var v : neighbors)
                inDegree.put(v, inDegree.get(v) + 1);

        PriorityQueue<String> queue = new PriorityQueue<>();
        List<String> result = new ArrayList<>();
        for(String v : vertices)
            if(inDegree.get(v) == 0)
                queue.add(v);

        while(!queue.isEmpty()) {
            String v = queue.poll();
            result.add(v);

            if(!graph.containsKey(v))
                continue;

            for(String neighbor : graph.get(v)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if(inDegree.get(neighbor) == 0)
                    queue.add(neighbor);
            }
        }

        if(result.size() != vertices.size())
            throw new IllegalStateException("В графе есть цикл.");
        return result;
    }
}
