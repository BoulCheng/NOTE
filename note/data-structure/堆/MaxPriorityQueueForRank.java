package so.dian.spider.job.test;




public class MaxPriorityQueueForRank<E> {

    private int capacity;
    private Object[] queue;
    private int size;


    public MaxPriorityQueueForRank(int size) {
        this.capacity = size;
        this.queue = new Object[size];
    }


    public int size() {
        return this.size;
    }

    @SuppressWarnings("unchecked")
    public boolean offer(E e) {
        if (e == null) {
            return false;
        }
        if (size >= capacity) {
            if (peek() != null && ((Comparable<E>) e).compareTo(peek()) < 0) {
                queue[0] = null;
                siftDown(0, e);
            }
            return false;
        }
        if (size == 0) {
            queue[0] = e;
        } else {
            siftUp(size, e);
        }
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E e) {
        Comparable<E> key = (Comparable<E>) e;
        while (k > 0) {
            int i = (k - 1) >>> 1;
            E parent = (E) queue[i];
            if (key.compareTo(parent) <= 0) {
                break;
            } else {
                queue[k] = parent;
                k = i;
            }
        }
        queue[k] = e;
    }


    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) {
            return null;
        }
        E e = (E) queue[0];
        queue[0] = null;
        int k = --size;
        E last = (E) queue[k];
        queue[k] = null;


        //必须在调用前先维护size
        //最后一个元素不需要在调用siftDown

        if (k != 0) {
            siftDown(0, last);
        }

        return e;
    }


    /**
     *         //必须在调用前先维护size
     *         //最后一个元素不需要在调用siftDown
     * @param k
     * @param e
     */
    @SuppressWarnings("unchecked")
    private void siftDown(int k, E e) {
        //最后一个叶子节点
        //也可以采用 PriorityQueue 的 siftDown方式， size >>> 1 即使是根节点计算出来的父结点也是本身, 在while循环时通过小于而不是小于等可以统一处理只剩一个结点的情况 即只剩一个结点时不会进入while循环
        if ((size - 1) == 0) {
            queue[k] = e;
            return;
        }
        int lastNonLeaf = ((size - 1) - 1) >>> 1;
        Comparable<E> key = (Comparable<E>) e;

        //遍历到最后一个非叶子节点
        while (k <= lastNonLeaf) {
            int i = (k << 1) + 1;
            E x = (E) queue[i];
            int right = i + 1;

            //最后一个非叶子节点可能没有右孩子但一定会有左孩子
            if (right < size && ((Comparable<E>) queue[right]).compareTo(x) > 0) {
                i = right;
                x = (E) queue[right];
            }

            if (key.compareTo(x) >= 0) {
                break;
            } else {
                queue[k] = x;
                k = i;
            }
        }

        queue[k] = e;
    }


    public E peek() {
        if (size == 0) {
            return null;
        }
        return (E)queue[0];
    }


    public static void main(String[] args) {


        /**
         * 求最小的n个元素
         */
        MaxPriorityQueueForRank<Integer> maxPriorityQueue = new MaxPriorityQueueForRank<>(3);


        maxPriorityQueue.offer(4);
        maxPriorityQueue.offer(9);
        maxPriorityQueue.offer(0);
        maxPriorityQueue.offer(7);
        maxPriorityQueue.offer(5);
        maxPriorityQueue.offer(1110);
        maxPriorityQueue.offer(110);
        maxPriorityQueue.offer(3);
        maxPriorityQueue.offer(100);
        maxPriorityQueue.offer(200);
        maxPriorityQueue.offer(19);
        maxPriorityQueue.offer(1);

        while (maxPriorityQueue.peek() != null) {
            System.out.println(maxPriorityQueue.poll());
        }
    }



}
