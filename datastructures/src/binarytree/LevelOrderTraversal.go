package binarytree

import "fmt"

func (root *BinaryTreeNode) levelOrderTraversalOfBinaryTree() {
	visited := []*BinaryTreeNode{}
	queue := []*BinaryTreeNode{root}

	for len(queue) != 0 {
		node := queue[0]
		queue = queue[1:len(queue)]
		visited = append(visited, node)
		if node.Left != nil {
			queue = append(queue, node.Left)
		}
		if node.Right != nil {
			queue = append(queue, node.Right)
		}
	}

	// Result should be in visited[] array
	for i := 0; i < len(visited); i++ {
		fmt.Printf(" %v,", visited[i].Data)
	}
}
