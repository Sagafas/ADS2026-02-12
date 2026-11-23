package by.it.group510901.petsevich.lesson14;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class SitesB {
    private static class DSU {
        private final int[] parent;
        private final int[] size;

        DSU(int count) {
            parent = new int[count];
            size = new int[count];
            for (int i = 0; i < count; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int findSet(int x) {
            if (parent[x] != x)
                parent[x] = findSet(parent[x]);
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = findSet(x);
            int rootY = findSet(y);

            if (rootX == rootY)
                return;

            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        int idx = 0;
        Map<String, Integer> sitesToIdx = new HashMap<>();
        List<int[]> linkedSites = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.equals("end")) break;

            int i = 0, j = 0;
            String[] parts = line.split(Pattern.quote("+"));
            if(!sitesToIdx.containsKey(parts[0])) {
                sitesToIdx.put(parts[0], idx);
                i = idx++;
            }
            else i = sitesToIdx.get(parts[0]);
            if(!sitesToIdx.containsKey(parts[1])) {
                sitesToIdx.put(parts[1], idx);
                j = idx++;
            }
            else j = sitesToIdx.get(parts[1]);

            int[] link = new int[2];
            link[0] = i; link[1] = j;
            linkedSites.add(link);
        }

        DSU dsu = new DSU(idx);
        for(var link : linkedSites)
            dsu.union(link[0], link[1]);

        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < idx; i++) {
            if(dsu.findSet(i) == i)
                result.add(dsu.size[i]);
        }
        result.sort(Comparator.reverseOrder());

        for (int clusterSize : result)
            System.out.print(clusterSize + " ");
    }
}
