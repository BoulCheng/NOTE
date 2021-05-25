```
    public Node rightRotate(Node root, Node target) {
        if (target.left != null) {
            Node tLeft = target.left;

            target.left = tLeft.right;
            if (tLeft.right != null) {
//                target.left = tLeft.right; // 不能放在if里面 target.left一定要更新
                tLeft.right.parent = target;
            }

            tLeft.parent = target.parent;
            if (target.parent != null) {
//                tLeft.parent = target.parent; // 不能放在if里面 tLeft.parent一定要更新
                if (target == target.parent.left) {
                    target.parent.left = tLeft;
                } else {
                    target.parent.right = tLeft;
                }
            } else {
                root = tLeft;
                root.red = false;
            }

            target.parent = tLeft;
            tLeft.right = target;
        }
        return root;
    }
```