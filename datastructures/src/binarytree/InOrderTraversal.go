package binarytree

import "fmt"

/* InOrder Traversal of BinaryTree */
func inOrderTraversalOfBinaryTree(root *BinaryTreeNode) {
	if root == nil {
		return
	}

	inOrderTraversalOfBinaryTree(root.Left)
	fmt.Printf(" %v,", root.Data)
	inOrderTraversalOfBinaryTree(root.Right)
}
