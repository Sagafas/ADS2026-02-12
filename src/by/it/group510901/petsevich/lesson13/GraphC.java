package by.it.group510901.petsevich.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        TreeSet<String> vertices = new TreeSet<>();
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Set<String>> rGraph = new HashMap<>();

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

                if (!rGraph.containsKey(to)) {
                    HashSet<String> neighbors = new HashSet<>();
                    neighbors.add(from);
                    rGraph.put(to, neighbors);
                } else rGraph.get(to).add(from);
            }
        }

        List<String> result = topologicalSort(rGraph, vertices);
        List<Set<String>> components = getComponents(graph, result);

        for(var component : components) {
            for(String v : component)
                System.out.print(v);
            System.out.println();
        }
    }

    private static List<String> topologicalSort(Map<String, Set<String>> graph, Set<String> vertices) {
        List<String> order = new ArrayList<>();
        Map<String, Boolean> used = new HashMap<>();
        for(String v : vertices)
            used.put(v, false);

        for(String v : vertices)
            if(!used.get(v))
                dfsOrder(v, graph, used, order);

        Collections.reverse(order);
        return order;
    }

    private static void dfsOrder(String v, Map<String, Set<String>> graph,
                                      Map<String, Boolean> used, List<String> order) {
        if(used.get(v))
            return;

        used.put(v, true);
        if(graph.containsKey(v)) {
            for (var neighbor : graph.get(v))
                dfsOrder(neighbor, graph, used, order);
        }
        order.add(v);
    }

    private static List<Set<String>> getComponents(Map<String, Set<String>> graph, List<String> result) {
        Map<String, Boolean> visited = new HashMap<>();
        for(String v : result)
            visited.put(v, false);

        List<Set<String>> components = new ArrayList<>();
        for(String v : result) {
            if(!visited.get(v)) {
                Set<String> component = new TreeSet<>();
                dfsComponents(v, graph, visited, component);
                components.add(component);
            }
        }

        Collections.reverse(components);
        return components;
    }

    private static void dfsComponents(String v, Map<String, Set<String>> graph,
                                      Map<String, Boolean> visited, Set<String> component) {
        if(visited.get(v))
            return;

        visited.put(v, true);
        component.add(v);
        if(graph.containsKey(v)) {
            for (var neighbor : graph.get(v))
                dfsComponents(neighbor, graph, visited, component);
        }
    }
}
