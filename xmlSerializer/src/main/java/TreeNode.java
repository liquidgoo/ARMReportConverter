public class TreeNode<E> {
    TreeNode<E> child;
    TreeNode<E> sibling;

    E value;

    public TreeNode(E value) {
        this.value = value;
    }
}
