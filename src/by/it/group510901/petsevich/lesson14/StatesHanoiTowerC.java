package by.it.group510901.petsevich.lesson14;

import java.io.IOException;
import java.util.Scanner;

public class StatesHanoiTowerC {
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

    static int step, heightA, heightB, heightC;
    static int[] first;
    static DSU dsu;
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        heightA = n;
        heightB = 0;
        heightC = 0;
        step = 0;

        first = new int[n+1];
        for(int i = 1; i <= n; i++)
            first[i] = -1;

        int amountOfSteps = Math.powExact(2, n) - 1;
        dsu = new DSU(amountOfSteps);
        hanoi(n, 1, 2, 3);

        int count = 0;
        int[] sizes = new int[amountOfSteps];
        for(int i = 0; i < amountOfSteps; i++)
            if(dsu.parent[i] == i)
                sizes[count++] = dsu.size[i];

        for(int i = 0; i < count-1; i++) {
            for(int j = 0; j < count-i-1; j++) {
                if(sizes[j] > sizes[j+1]) {
                    int tmp = sizes[j];
                    sizes[j] = sizes[j+1];
                    sizes[j+1] = tmp;
                }
            }
        }

        for(int i = 0; i < count; i++)
            System.out.print(sizes[i] + " ");
    }

    private static void hanoi(int n, int from, int to, int additional) {
        if(n == 0) return;
        hanoi(n-1, from, additional, to);

        moveDisk(from, to);
        int height = getMaxHeight();
        int heightToStep = first[height];
        if(first[height] != -1)
            dsu.union(step++, heightToStep);
        else first[height] = step++;

        hanoi(n-1, additional, to, from);
    }

    private static void moveDisk(int from, int to) {
        decreaseHeight(from);
        increaseHeight(to);
    }

    private static void decreaseHeight(int disk) {
        switch (disk) {
            case 1: heightA--; break;
            case 2: heightB--; break;
            case 3: heightC--; break;
        }
    }

    private static void increaseHeight(int disk) {
        switch (disk) {
            case 1: heightA++; break;
            case 2: heightB++; break;
            case 3: heightC++; break;
        }
    }

    private static int getMaxHeight() {
        return Math.max(heightA, Math.max(heightB, heightC));
    }
}
