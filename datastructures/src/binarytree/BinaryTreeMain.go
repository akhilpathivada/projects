package binarytree

import "fmt"

/* Creating new Binary Tree Node */
func newBinaryTreeNode(value int) *BinaryTreeNode {
	return &BinaryTreeNode{
		Data:  value,
		Left:  nil,
		Right: nil,
	}
}

/* Creating Binary Tree */
func createBinaryTree(rootAddress **BinaryTreeNode) {
	root := newBinaryTreeNode(4)
	root.Left = newBinaryTreeNode(2)
	root.Left.Left = newBinaryTreeNode(1)
	root.Left.Right = newBinaryTreeNode(3)
	root.Right = newBinaryTreeNode(6)
	root.Right.Left = newBinaryTreeNode(5)
	root.Right.Right = newBinaryTreeNode(8)
	root.Left.Left.Left = newBinaryTreeNode(0)
	root.Right.Right.Left = newBinaryTreeNode(7)

	*rootAddress = root
}

func BinaryTreeMain() {
	var root *BinaryTreeNode
	createBinaryTree(&root)

	fmt.Print("\n InOrder Traversal : ")
	inOrderTraversalOfBinaryTree(root)

	fmt.Print("\n Level Order Traversal : ")
	root.levelOrderTraversalOfBinaryTree()
}
