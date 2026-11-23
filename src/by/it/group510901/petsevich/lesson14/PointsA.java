package by.it.group510901.petsevich.lesson14;

import java.io.IOException;
import java.util.*;

public class PointsA {
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

        if (!scanner.hasNextInt()) return;
        long d = scanner.nextLong();
        int count = scanner.nextInt();

        int[][] points = new int[count][3];
        for (int i = 0; i < count; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        DSU dsu = new DSU(count);
        long sqD = (long) d * d;
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                long dx = points[i][0] - points[j][0];
                long dy = points[i][1] - points[j][1];
                long dz = points[i][2] - points[j][2];
                long sqDistance = dx * dx + dy * dy + dz * dz;
                if (sqDistance < sqD)
                    dsu.union(i, j);
            }
        }

        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            if(dsu.findSet(i) == i)
                result.add(dsu.size[i]);
        }
        result.sort(Comparator.reverseOrder());

        for(int clusterSize : result)
            System.out.print(clusterSize + " ");
    }
}
