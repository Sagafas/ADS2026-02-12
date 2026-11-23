package by.it.group510901.petsevich.lesson13;

import java.io.Console;
import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        TreeSet<String> vertices = new TreeSet<>();
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

        Map<String, Integer> visited = new HashMap<>();
        for(String v : vertices)
            visited.put(v, 0);

        for(String v : vertices) {
            if (visited.get(v) == 0 && cycleDfs(v, graph, visited)) {
                System.out.println("yes");
                return;
            }
        }
        System.out.println("no");
    }

    private static boolean cycleDfs(String v, Map<String, Set<String>> graph, Map<String, Integer> visited) {
        int state = visited.get(v);
        if(state == 1) return true;
        if(state == 2) return false;

        visited.put(v, 1);
        if(graph.containsKey(v)) {
            for (var neighbor : graph.get(v))
                if (cycleDfs(neighbor, graph, visited))
                    return true;
        }

        visited.put(v, 2);
        return false;
    }
}


