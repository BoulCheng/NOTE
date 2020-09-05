```

class Skiplist {

    Node head = new Node(null, null, 0);
    Random random = new Random();
    double p = 1 / 4D;
    int maxLevel = 32; // skiplist跳表期望层数为log(1/p)n  p=0.25 level=32 期望总结点数为4^32;  最高层的平均节点数为1/p
    Node[] pathNodes = new Node[maxLevel]; //查询过程保存的结点个数等于层数


    public Skiplist() {

    }

    public boolean search(int target) {
        for (Node p = head; p != null; p = p.down) {
            while (p.right != null && p.right.val < target) {
                p = p.right;
            }
            if (p.right != null && p.right.val == target) {// p.right.val >= target && p.right.val <= target 即 p.right.val = target
                return true;
            }
        }
        return false;
    }

    public boolean erase(int num) {
        boolean erase = false;
        for (Node p = head; p != null; p = p.down) {
            while (p.right != null && p.right.val < num) {
                p = p.right;
            }

            if (p.right != null && p.right.val == num) {// p.right.val >= target && p.right.val <= target 即 p.right.val = target
                if (!erase) {
                    erase = true;
                }
                p.right = p.right.right;
            }
        }
        return erase;
    }

    public void add(int num) {
        int i = -1;
        for (Node p = head; p != null; p = p.down) {//从上层往下层遍历
            while (p.right != null && p.right.val < num) {//每层从左往右遍历
                p = p.right;
            }
            pathNodes[++i] = p;
        }

        int nodeLevel = getLevel();
        boolean newLevel = nodeLevel > i;

        Node downNode = null;
        Node pathNode;
        for (; i >= 0 && nodeLevel >= 0; i--, nodeLevel--) {
            pathNode = pathNodes[i];
            pathNode.right = new Node(pathNode.right, downNode, num);
            downNode = pathNode.right;
        }

        // 新增结点的层数最多为原来的跳表层数+1
        if (newLevel) {
            head = new Node(new Node(null, downNode, num), head, 0);
        }
    }


    private int getLevel() {
        // 0 表示1层
        return randomLevel() - 1;
    }

    private int randomLevel() {

        int i = 1; // i=1的概率为 (1-p)
        while (random.nextDouble() < p && i < maxLevel) {
            i++;
            //random.nextDouble()  均匀分布
            // the next pseudorandom, uniformly distributed {@code double} value between {@code 0.0} and {@code 1.0}
        }
        return i;
    }


    static class Node {
        int val;
        Node right, down;

        public Node(Node r, Node d, int val) {
            right = r;
            down = d;
            this.val = val;
        }
    }
}

```